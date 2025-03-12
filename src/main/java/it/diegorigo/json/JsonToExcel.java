package it.diegorigo.json;

import it.diegorigo.date.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonToExcel {

    private final static String PATH = "D:\\Workspaces\\Github\\my-website\\src\\assets\\json\\giochi.json";

    public static void main(String[] args) throws IOException {
        Path path = Paths.get(PATH);
        String fileContent = Files.readString(path);
        try (Workbook workbook = new XSSFWorkbook()) {
            JSONArray jsonArray = new JSONArray(fileContent);
            Sheet sheet = workbook.createSheet("Dati");

            writeHeader(sheet, jsonArray);
            writeRows(jsonArray, sheet);
            writeOutput(path, workbook);

            System.out.println("Excel creato con successo!");
        }
    }

    private static void writeRows(JSONArray jsonArray,
                                  Sheet sheet) {
        int rowIndex = 1;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            for (String key : jsonObject.keySet()) {
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(jsonObject.getString(key));
            }
        }
    }

    private static void writeHeader(Sheet sheet,
                                    JSONArray jsonArray) {
        Row headerRow = sheet.createRow(0);
        JSONObject firstObject = jsonArray.getJSONObject(0);
        int cellIndex = 0;
        for (String key : firstObject.keySet()) {
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(key);
        }
    }

    private static void writeOutput(Path path,
                                    Workbook workbook) throws IOException {
        String filename = "output-" + DateUtils.isoDateTime() + ".xlsx";
        File outputFile = path.getParent().resolve(filename).toFile();
        FileOutputStream os = new FileOutputStream(outputFile);
        workbook.write(os);
    }
}
