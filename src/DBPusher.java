import classes.Student;
import com.opencsv.exceptions.CsvValidationException;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.LinkedHashSet;

public class DBPusher {

    private ParseCSV csvData;
    private DBConfigManager db;
    private LinkedHashSet<Student> studentLocalStorage;
    public DBPusher() throws CsvValidationException, IOException, SQLException, ClassNotFoundException {
        this.csvData = new ParseCSV("basicprogramming.csv");
        this.db = new DBConfigManager();
        this.studentLocalStorage = csvData.parseStudents();
    }
/*    public void addStudents() throws InterruptedException {
        for (Student student: studentLocalStorage
             ) {
            db.AddStudent(student);
            Thread.sleep(100);
        }
    }*/
    public void createTables() throws SQLException, ClassNotFoundException, IOException, URISyntaxException, ParseException, InterruptedException {
        var br = new BufferedReader(new FileReader("token.txt"));
        var token = br.readLine();
        var csvParser = new ParseCSV("basicprogramming.csv");
        var apiParser = new UlearnParse(token);
        var practices = apiParser.getAllPractices();
        var exercises = apiParser.getAllExercises();
        db.createTableThemes();
        db.createTablePractices();
        db.createTableExercises();
        db.createTablePracticesScores(practices);
        db.createTableExercisesScores(exercises);
        db.createTableThemesPracticesAndExercises();
    }

    public void addScoreIntoTables () throws IOException, URISyntaxException, ParseException, InterruptedException, SQLException, ClassNotFoundException, CsvValidationException {
        var br = new BufferedReader(new FileReader("token.txt"));
        var token = br.readLine();
        var csvParser = new ParseCSV("basicprogramming.csv");
        var apiParser = new UlearnParse(token);
        var practices = apiParser.getAllPractices();
        var exercises = apiParser.getAllExercises();
        var themes = apiParser.getAllThemes();
        var practicesScores = csvParser.parseUsersPracticesScores(practices);
        db.addThemes(themes);
        db.addExercises(exercises);
        /*db.addPractices(practices);
        db.addPracticesScores(practicesScores);*/
        var exercisesScores = csvParser.parseUsersExercisesScores(exercises);
        db.addExercisesScores(exercisesScores);

    }
}
