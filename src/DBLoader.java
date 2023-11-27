import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class DBLoader extends Configs{
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }
    public void AddStudent(String[] studentinfo){
        String insert = "INSERT INTO " + Const.STUDENTS_TABLE + "(" +
                Const.STUDENTS_FIRSTNAME + "," + Const.STUDENTS_LASTNAME +
                "," + Const.STUDENTS_PATRONYMIC + "," + Const.STUDENTS_ULEARNID + "," + Const.STUDENTS_EMAIL +
                "," + Const.STUDENTS_GROUP + ")"  + " VALUES(?,?,?,?,?,?)" + ";";
        try {
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, studentinfo[0]);
        prSt.setString(2, "null");
        prSt.setString(3, "null");
        prSt.setString(4, "null");//studentinfo[1].replace('-',' '));
        prSt.setString(5, "null");//studentinfo[2]);
        prSt.setString(6, "null");//studentinfo[2]);


            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
