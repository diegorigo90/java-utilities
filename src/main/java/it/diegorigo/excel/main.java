package it.diegorigo.excel;

import it.diegorigo.excel.annotations.ExcelInfo;
import it.diegorigo.excel.dto.ExcelPageInfo;
import it.diegorigo.excel.dto.TestClass;
import it.diegorigo.excel.dto.TestClass2;
import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;

import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class main {

    private static class Test {
        @ExcelInfo(position = 0, title = "Time")
        LocalTime time;

        public Test(LocalTime time) {
            this.time = time;
        }
    }

    public static void main(String[] args) throws UtilityException {
        //        byte[] bytes = ExcelUtils.generateExcel(List.of(new Test(LocalTime.of(5, 0, 0))));
        //        FilesUtils.toFile(bytes, Paths.get("C:\\test-input\\test.ods"));7


        List<ExcelPageInfo<?>> info = new ArrayList<>();
        info.add(new ExcelPageInfo<>("Info", List.of(new TestClass("Ciccio", "Pasticcio", 2), new TestClass("Livio", "Tonali", 15), new TestClass("Marilena", "Bomber", 4))));
        info.add(new ExcelPageInfo<>("Lista", List.of(new TestClass2("rosso", 2), new TestClass2("giallo", 5))));
        byte[] bytes = ExcelUtils.generateExcelFromPagesInfo(info);
//        FilesUtils.toFile(bytes, Paths.get("C:\\test\\test.xlsx"));
        FilesUtils.toFile(bytes, Paths.get("C:\\test\\test.ods"));
    }
}
