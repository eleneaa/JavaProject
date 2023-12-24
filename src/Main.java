import java.io.BufferedReader;
import java.io.FileReader;

import classes.Course;
import classes.Exercise;
import classes.Practice;
import classes.Student;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        DBPusher pusher = new DBPusher();
       /* pusher.addStudents();*/
        pusher.createTables();


    }
}