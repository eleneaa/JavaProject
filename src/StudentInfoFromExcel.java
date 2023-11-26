import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.*;

public class StudentInfoFromExcel {
    public static void main(String[] args) throws IOException{
        String[] students = new String[1035];

        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream("C:/Users/Пользователь/Downloads/basicprogramming.xls"));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("Основы программирования");
        for (int i = 0; i<1033; i++) {
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
            //System.out.println(Arrays.toString(studentInfo));
            students[i] = Arrays.toString(studentInfo);
        }
        for (String student: students){
            System.out.println(student+"\n");
        }
        myExcelBook.close();

    }
}


