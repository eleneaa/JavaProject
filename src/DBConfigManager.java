import classes.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class DBConfigManager extends Configs{
    Connection dbConnection;

    public DBConfigManager() throws SQLException, ClassNotFoundException {
        this.dbConnection = getDbConnection();
    }

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");
        //dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return DriverManager.getConnection(connectionString, dbUser, dbPassword);
    }
    public void AddStudent(Student studentinfo)
    {
        String insert = "INSERT INTO " + Const.STUDENTS_TABLE + "(" +
                Const.STUDENTS_FIRSTNAME + "," + Const.STUDENTS_LASTNAME +
                 "," + Const.STUDENTS_ULEARNID + "," + Const.STUDENTS_EMAIL +
                "," + Const.STUDENTS_GROUP + ")"  + " VALUES(?,?,?,?,?)" + ";";
        try
        {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, studentinfo.firstname);
            prSt.setString(2, studentinfo.lastname);
            prSt.setString(3, studentinfo.ulearnID);
            prSt.setString(4, studentinfo.email);
            prSt.setString(5, studentinfo.group);
            prSt.executeUpdate();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void AddProgress(String[] progressinfo)
    {
        String insert = "INSERT INTO " + Const.PROGRESS_TABLE + "(" + Const.PROGRESS_ULEARNID+ "," +
                Const.PROGRESS_ACTIVITIES+ "," + Const.PROGRESS_EXERSISES +
                "," + Const.PROGRESS_HOMEWORKS + "," + Const.PROGRESS_SEMINARS +
                 ")"  + " VALUES(?,?,?,?,?)" + ";";
        try
        {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, progressinfo[0]);
            prSt.setString(2, progressinfo[1]);
            prSt.setString(3, progressinfo[2]);
            prSt.setString(4, progressinfo[3]);
            prSt.setString(5, progressinfo[4]);
            prSt.executeUpdate();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void createTableThemes() throws SQLException, ClassNotFoundException {
        var query = "CREATE TABLE IF NOT EXISTS Themes (id VARCHAR(100) PRIMARY KEY, title VARCHAR(244));";
        PreparedStatement prSt = getDbConnection().prepareStatement(query);
        prSt.execute(query);
    }

    public void createTableExercises() throws SQLException, ClassNotFoundException {
        var query = "CREATE TABLE IF NOT EXISTS Exercises (id VARCHAR(100) PRIMARY KEY, title VARCHAR(244));";
        PreparedStatement prSt = getDbConnection().prepareStatement(query);
        prSt.execute(query);
    }

    public void createTablePractices() throws SQLException, ClassNotFoundException {
        var query = "CREATE TABLE IF NOT EXISTS Practices (id VARCHAR(100) PRIMARY KEY, title VARCHAR(244));";
        PreparedStatement prSt = getDbConnection().prepareStatement(query);
        prSt.execute(query);
    }
    public void createTableExercisesScores(ArrayList<Exercise> exercises) throws SQLException, ClassNotFoundException {
        var ids = exercises.stream().map(Exercise::getId).collect(Collectors.toList());
        createTableWithSameTypeColumnsAndUID("ExercisesScores", ids, "TINYINT");
    }

    public void createTablePracticesScores(ArrayList<Practice> practices) throws SQLException, ClassNotFoundException {
        var ids = practices.stream().map(Practice::getId).collect(Collectors.toList());
        createTableWithSameTypeColumnsAndUID("PracticesScores", ids, "TINYINT");
    }

    public void createTableWithSameTypeColumnsAndUID(String tableName, List<String> columnsNames, String type) throws SQLException, ClassNotFoundException {
        var builder = new StringBuilder();
        builder.append(String.format("CREATE TABLE IF NOT EXISTS %s (UlearnID VARCHAR(100) PRIMARY KEY", tableName));
        for (var id: columnsNames) {
            id = "exercise_"+id;
            id = id.replace('-', '_');
            builder.append(String.format(", %s %s", id, type));
        }
        builder.append(");");
        PreparedStatement prSt = getDbConnection().prepareStatement(builder.toString());
        prSt.execute(builder.toString());
    }

    public void createTableThemesPracticesAndExercises() throws SQLException, ClassNotFoundException {
        var query = "CREATE TABLE IF NOT EXISTS ThemesPracticesAndExercises (id VARCHAR(100) PRIMARY KEY, practices TEXT, exercises TEXT);";
        PreparedStatement prSt = getDbConnection().prepareStatement(query);
        prSt.execute(query);
    }

    //add info
    private <T extends CourseElement> void addIdTitle(ArrayList<T> elements, String tableName)
            throws SQLException, ClassNotFoundException {
        var count = 0;
        for (var element: elements) {
            var query = String.format(
                    "REPLACE INTO %s (id, title) VALUES ('%s', '%s')",
                    tableName,
                    element.getId(),
                    element.getTitle());
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
            prSt.execute(query);
            count++;
            if (count > 500) {
                prSt.executeBatch();
                count = 0;
            }
        }
    }

    /*public  <T extends CourseElement>  HashMap<String, HashMap<T, Integer>> getUidScores(ArrayList<T> elems, String tableName)
            throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();
        var q = String.format("SELECT * FROM %s", tableName);
        var qRes = st.executeQuery(q);
        var res = new HashMap<String, HashMap<T, Integer>>();
        while (qRes.next()){
            var uid = qRes.getString("UlearnID");
            var scores = new HashMap<T, Integer>();
            for (var elem: elems) {
                var score = qRes.getInt(elem.getId());
                scores.put(elem, score);
            }
            res.put(uid, scores);
        }
        return res;
    }*/

    private <T extends CourseElement> void addUidScores(
            HashMap<String, HashMap<T, Integer>> uidScores,
            String tableName
    ) throws SQLException, ClassNotFoundException {
        var paramsBuilder = new StringBuilder();
        var valuesBuilder = new StringBuilder();
        var count = 0;
        for (var uid : uidScores.keySet()) {
            paramsBuilder.setLength(0);
            valuesBuilder.setLength(0);
            var scores = uidScores.get(uid);
            for (var elem : scores.keySet()) {
                var score = scores.get(elem);
                paramsBuilder.append(String.format("exercise_%s, ", elem.getId()));
                valuesBuilder.append(String.format("%d, ", score));
            }

            var query = String.format("REPLACE INTO %s (UlearnID, %s) VALUES ('%s', %s);",
                    tableName,
                    paramsBuilder.substring(0, paramsBuilder.length() - 2).replace('-', '_'),
                    uid,
                    valuesBuilder.substring(0, valuesBuilder.length() - 2));
            PreparedStatement prSt = getDbConnection().prepareStatement(query);
            prSt.execute(query);
            count++;

            if (count > 500) {
                prSt.executeBatch();
                count = 0;
            }
        }
    }
    public void addThemes(ArrayList<Theme> themes) throws SQLException, ClassNotFoundException {
        addIdTitle(themes, "Themes");
        for (var theme: themes) {
            var jsonArrExercises = new JSONArray();
            jsonArrExercises.addAll(theme.getExercises().stream().map(e -> e.getId()).toList());
            var jsonArrPractices = new JSONArray();
            jsonArrPractices.addAll(theme.getPractices().stream().map(p -> p.getId()).toList());
            var q = String.format("REPLACE INTO ThemesPracticesAndExercises (id, exercises, practices) VALUES ('%s', '%s', '%s')",
                    theme.getId(),
                    jsonArrExercises.toJSONString(),
                    jsonArrPractices.toJSONString());
            PreparedStatement prSt = getDbConnection().prepareStatement(q);
            prSt.execute(q);
        }

    }
    public void addExercises(ArrayList<Exercise> exercises) throws SQLException, ClassNotFoundException {
        addIdTitle(exercises, "Exercises");
    }

    public void addPractices(ArrayList<Practice> practices) throws SQLException, ClassNotFoundException {
        addIdTitle(practices, "Practices");
    }

    public void addPracticesScores(HashMap<String, HashMap<Practice, Integer>> allScores) throws SQLException, ClassNotFoundException {
        addUidScores(allScores, "PracticesScores");
    }

    public void addExercisesScores(HashMap<String, HashMap<Exercise, Integer>> allScores) throws SQLException, ClassNotFoundException {
        addUidScores(allScores, "ExercisesScores");
    }
    public ArrayList<Practice> getPractices() throws SQLException, ClassNotFoundException {
        var q = "SELECT id, title FROM Practices;";
        Statement st = getDbConnection().createStatement();
        var queryRes = st.executeQuery(q);
        var result = new ArrayList<Practice>();
        while (queryRes.next()){
            var id = queryRes.getString("id");
            var title = queryRes.getString("title");
            result.add(new Practice(title, id));
        }
        return result;
    }

    /**
     * Возвращает список всех упражнений из базы
     */
    public ArrayList<Exercise> getExercises() throws SQLException, ClassNotFoundException {
        var q = "SELECT id, title FROM Exercises;";
        Statement st = getDbConnection().createStatement();
        var queryRes = st.executeQuery(q);
        var result = new ArrayList<Exercise>();
        while (queryRes.next()){
            var id = queryRes.getString("id");
            var title = queryRes.getString("title");
            result.add(new Exercise(title, id));
        }
        return result;
    }

    private HashMap<String, String> getThemesTitles() throws SQLException, ClassNotFoundException {
        var q = "SELECT id, title FROM Themes;";
        Statement st = getDbConnection().createStatement();
        var queryRes = st.executeQuery(q);
        var result = new HashMap<String, String>();
        while (queryRes.next()){
            var id = queryRes.getString("id");
            var title = queryRes.getString("title");
            result.put(id, title);
        }
        return result;
    }

    /**
     * Возвращает список всех тем из базы
     */
    public ArrayList<Theme> getThemes() throws SQLException, ParseException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();
        var titles = getThemesTitles();
        var themes = new ArrayList<Theme>();
        var allExercises = getExercises();
        var allPractices = getPractices();
        var q = "SELECT * FROM ThemesPracticesAndExercises;";
        var queryRes = st.executeQuery(q);
        while (queryRes.next()){
            var id = queryRes.getString("id");
            var title = titles.get(id);

            var parser = new JSONParser();
            var jsonExercises = (JSONArray) parser.parse(queryRes.getString("exercises"));
            var jsonPractices = (JSONArray) parser.parse(queryRes.getString("practices"));
            var practicesIds = new HashSet<String>(jsonPractices);
            var exercisesIds = new HashSet<String>(jsonExercises);
            var practices = new ArrayList<>(allPractices.stream().filter(p -> practicesIds.contains(p.getId())).toList());
            var exercises = new ArrayList<>(allExercises.stream().filter(e -> exercisesIds.contains(e.getId())).toList());

            themes.add(new Theme(title, id, practices, exercises));
        }
        return themes;
    }

    /**
     * Возвращает список всех студентов из базы
     */
    public ArrayList<Student> getStudents() throws SQLException, ClassNotFoundException {
        var q = "SELECT * FROM Students;";
        Statement st = getDbConnection().createStatement();
        var queryRes = st.executeQuery(q);
        var result = new ArrayList<Student>();
        while (queryRes.next()){
            var firstname = queryRes.getString("first_name");
            var lastname = queryRes.getString("last_name");
            var ulearnID = queryRes.getString("ulearn_ID");
            var email = queryRes.getString("email");
            var group = queryRes.getString("students_group");
            result.add(new Student(firstname, lastname, ulearnID, email, group));
        }
        return result;
    }

    public HashMap<String, HashMap<Exercise, Integer>> getUidExercisesScores() throws SQLException, ClassNotFoundException {
        var exercises = getExercises();
        return getUidScores(exercises, "ExercisesScores");
    }

    /**
     * Возвращает словарь: [ exercise - scoresList ]
     */
    public HashMap<Exercise, ArrayList<Integer>> getExercisesScores() throws SQLException, ClassNotFoundException {
        return getScores(getUidExercisesScores());
    }

    /**
     * Возвращает словарь словарей: [ uid - (practice - score) ]
     */
    public HashMap<String, HashMap<Practice, Integer>> getUidPracticesScores() throws SQLException, ClassNotFoundException {
        var practices = getPractices();
        return getUidScores(practices, "PracticesScores");

    }

    /**
     * Возвращает словарь: [ practice - scoresList ]
     */
    public HashMap<Practice, ArrayList<Integer>> getPracticesScores() throws SQLException, ClassNotFoundException {
        return getScores(getUidPracticesScores());
    }

    private <T extends CourseElement>  HashMap<String, HashMap<T, Integer>> getUidScores(ArrayList<T> elems, String tableName)
            throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();
        var q = String.format("SELECT * FROM %s", tableName);
        var qRes = st.executeQuery(q);
        var res = new HashMap<String, HashMap<T, Integer>>();
        while (qRes.next()){
            var uid = qRes.getString("UlearnID");
            var scores = new HashMap<T, Integer>();
            for (var elem: elems) {
                var score = qRes.getInt(tableName.toLowerCase().substring(0, 8) + "_" + elem.getId().replace('-','_'));
                scores.put(elem, score);
            }
            res.put(uid, scores);
        }
        return res;
    }

    private <T extends CourseElement> HashMap<T, ArrayList<Integer>> getScores(HashMap<String, HashMap<T, Integer>> uidScores) {
        var result = new HashMap<T, ArrayList<Integer>>();
        for (var map: uidScores.values()) {
            for (var item: map.entrySet()) {
                var key = item.getKey();
                var score = item.getValue();
                if (result.containsKey(key)) {
                    result.get(key).add(score);
                } else {
                    result.put(key, new ArrayList<>(score));
                }
            }
        }
        return result;
    }

}


