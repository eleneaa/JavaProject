import classes.Exercise;
import classes.Practice;
import classes.Student;
import java.sql.*;
import java.util.ArrayList;
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
}
