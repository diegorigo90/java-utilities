package it.diegorigo.excel;

import it.diegorigo.excel.dto.ExcelPageInfo;
import it.diegorigo.excel.dto.TestClass;
import it.diegorigo.excel.dto.TestClass2;
import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) throws UtilityException {
        List<ExcelPageInfo<?>> info = new ArrayList<>();
        info.add(new ExcelPageInfo<>("Info", List.of(new TestClass("Ciccio", "Pasticcio", 2), new TestClass("Livio", "Tonali", 15), new TestClass("Marilena", "Bomber", 4))));
        info.add(new ExcelPageInfo<>("Lista", List.of(new TestClass2("rosso", 2), new TestClass2("giallo", 5))));
        byte[] bytes = ExcelUtils.generateExcelFromPagesInfo(info);
        FilesUtils.toFile(bytes, Paths.get("C:\\test\\test.ods"));
    }
}
