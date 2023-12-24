import classes.*;
import com.opencsv.exceptions.CsvValidationException;
import org.json.simple.parser.ParseException;

import java.net.URISyntaxException;
import java.sql.SQLException;

import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, ParseException, InterruptedException, CsvValidationException, SQLException, ClassNotFoundException {
        /*

        var apiParser = new UlearnParse(token);
        var exercises = apiParser.getAllExercises();
        */
        /*var csvparser = new ParseCSV("basicprogramming.csv");
        var br = new BufferedReader(new FileReader("token.txt"));
        var token = br.readLine();
        var apiParser = new UlearnParse(token);
        var practices = apiParser.getAllPractices();
        var practicesScores = csvparser.parseUsersPracticesScores(practices);
        for (var pr: practicesScores.values()) {
            System.out.println(pr.values());
        }*/
        /*DBConfigManager db = new DBConfigManager();
        DBPusher pusher = new DBPusher();
         pusher.addStudents();
        pusher.createTables();
        pusher.addScoreIntoTables();*/
        DBConfigManager db = new DBConfigManager();
        var students = db.getStudents();
        Charts chart = new Charts();

        //средний балл за упражнения, кроме 0
        var exersise = db.getExercisesScores();
        HashMap<String, Integer> avgScoreByExcersize = ChartDatasetBuilder.getAvgScorePerExcersise(exersise);
        chart.showChart(chart.getBarChart(avgScoreByExcersize,"Средний балл за упражнение", "Средний балл", "Упражнения"));

        //общий балл за упражнения
        HashMap<String, Integer> fullScoreByExcersize = ChartDatasetBuilder.getTotalScorePerExcersise(exersise);
        chart.showChart(chart.getBarChart(fullScoreByExcersize,"Общий балл за упражнение", "Общий балл", "Упражнения"));

        //средний балл по практикам
        var practice = db.getPracticesScores();
        HashMap<String, Integer> fullScoreByPractice = ChartDatasetBuilder.getAvgScorePerPractice(practice);
        chart.showChart(chart.getPieChart(fullScoreByPractice, "Общий балл за практики"));
        //chart.showChart(chart.getBarChart(fullScoreByPractice,"Общий балл за практики", "","" ));

        //общие баллы за практики по темам
        var themes = db.getThemes();
        HashMap<String, Integer> ThemesScores = ChartDatasetBuilder.getTotalPracticeScorePerTheme(themes, practice);
        chart.showChart(chart.getPieChart(ThemesScores, "Общие баллы за практики по темам"));

        //общие баллы за упражнения по темам
        var dbThemes = db.getThemes();
        HashMap<String, Integer> ThemesScoresEx = ChartDatasetBuilder.getTotalExcersiseScorePerTheme(dbThemes, exersise);
        chart.showChart(chart.getPieChart(ThemesScoresEx, "Общие баллы за упражнения по темам"));

        //лучшие 10 учеников
        var uidPracticeScore = db.getUidPracticesScores();
        var uidExerciseScore = db.getUidExercisesScores();

        HashMap<String, Integer> updateBestPupils = ChartDatasetBuilder.getTenBestStudents(uidPracticeScore, uidExerciseScore, students);
        chart.showChart(chart.getBarChart(updateBestPupils,"10 лучших учеников", "Кол-во баллов", "ФИО"));
    }
}