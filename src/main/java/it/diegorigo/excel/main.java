package it.diegorigo.excel;

import it.diegorigo.excel.annotations.ExcelInfo;
import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;

import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

public class main {

    private static class Test{
        @ExcelInfo(position = 0, title = "Time")
        LocalTime time;

        public Test(LocalTime time) {
            this.time = time;
        }
    }

    public static void main(String[] args) throws UtilityException {
        byte[] bytes = ExcelUtils.generateExcel(List.of(new Test(LocalTime.of(5, 0, 0))));
        FilesUtils.toFile(bytes, Paths.get("C:\\test-input\\test.ods"));
    }
}
