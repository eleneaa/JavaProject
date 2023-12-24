import org.apache.poi.hssf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;


public class StudentInfoFromExcel {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        var dbloader = new DBConfigManager();
        //AddStudents(dbloader);
        AddProgress(dbloader);

    }

    /*private static void AddStudents(DBLoader dbloader) throws IOException {
        String[] students = new String[1035];

        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream("./src/basicprogramming.xls"));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("Основы программирования");
        for (int i = 3; i<1033; i++) {
            HSSFRow row = myExcelSheet.getRow(i);
            String[] studentInfo;
            String str = new String();
            if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String name = row.getCell(0).getStringCellValue();
                str+=name+"; ";
            }
            if (row.getCell(1).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String ulearnID = row.getCell(1).getStringCellValue();
                str+=ulearnID+"; ";
            }
            if (row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String email = row.getCell(2).getStringCellValue();
                str+=email;
            }
            if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String group = row.getCell(3).getStringCellValue();
                str+="; " + group;
            }
            studentInfo = str.split("; ");
            if (studentInfo.length==4) dbloader.AddStudent(studentInfo);
            students[i-3] = Arrays.toString(studentInfo);
        }
        myExcelBook.close();
    }*/
    private static void AddProgress(DBConfigManager dbloader) throws IOException {
        String[] progress = new String[1035];

        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream("./src/basicprogramming.xls"));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("Основы программирования");
        for (int i = 3; i<1033; i++)
        {
            HSSFRow row = myExcelSheet.getRow(i);
            String[] progressInfo;
            String str = new String();
            if (row.getCell(1).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                String ulearnID = row.getCell(1).getStringCellValue();
                str+=ulearnID+"; ";
            }
            if (row.getCell(4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                Double name = row.getCell(4).getNumericCellValue();
                str+=name+"; ";
            }
            if (row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                Double ulearnID = row.getCell(5).getNumericCellValue();
                str+=ulearnID+"; ";
            }
            if (row.getCell(6).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                Double email = row.getCell(6).getNumericCellValue();
                str+=email;
            }
            if (row.getCell(7).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                Double group = row.getCell(7).getNumericCellValue();
                str+="; " + group;
            }
            progressInfo = str.split("; ");
            //System.out.println(Arrays.toString(progressInfo));
            if (progressInfo.length==5) dbloader.AddProgress(progressInfo);
            progress[i-3] = Arrays.toString(progressInfo);
        }
        myExcelBook.close();
    }
}


