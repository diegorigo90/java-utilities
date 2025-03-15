package it.diegorigo.excel;

import it.diegorigo.date.DateUtils;
import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonConverter {

    public static void toExcel(File file) throws UtilityException {
        JSONArray jsonArray = new JSONArray(FilesUtils.fileAsString(file));
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Dati");

            Row headerRow = sheet.createRow(0);
            JSONObject firstObject = jsonArray.getJSONObject(0);
            Iterator<String> keys = firstObject.keys();
            int colNum = 0;
            while (keys.hasNext()) {
                String key = keys.next();
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(key);
            }

            int rowNum = 1;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Row row = sheet.createRow(rowNum++);

                int cellNum = 0;
                Iterator<String> keysIterator = jsonObject.keys();
                while (keysIterator.hasNext()) {
                    String key = keysIterator.next();
                    Cell cell = row.createCell(cellNum++);
                    cell.setCellValue(jsonObject.getString(key));
                }
            }

            String fileName = String.join("_",
                                          DateUtils.isoDateTime(),
                                          FilesUtils.filenameWithoutExtension(file) + ".xlsx");
            File outputFile = file.toPath().getParent().resolve(fileName).toFile();
            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
                FilesUtils.openFile(outputFile);
            } catch (IOException e) {
                throw new UtilityException("Errore durante il salvataggio del file");
            }
        } catch (IOException e) {
            throw new UtilityException("Errore durante la conversione del file");
        }

    }

    public static void toJson(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            JSONArray jsonArray = new JSONArray();
            boolean firstRow = true;
            List<String> cols = new ArrayList<>();
            for (Row row : sheet) {
                if (firstRow) {
                    row.forEach(item -> cols.add(getCellValueAsString(item)));
                    firstRow = false;
                } else {
                    jsonArray.put(convertRowToJsonObject(row, cols));
                }
            }
            String jsonString = jsonArray.toString(2);
            FilesUtils.toFile(jsonString, file.toPath().getParent());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static JSONArray readAsJson(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            JSONArray jsonArray = new JSONArray();
            boolean firstRow = true;
            List<String> cols = new ArrayList<>();
            for (Row row : sheet) {
                if (firstRow) {
                    row.forEach(item -> cols.add(getCellValueAsString(item)));
                    firstRow = false;
                } else {
                    jsonArray.put(convertRowToJsonObject(row, cols));
                }
            }
            return jsonArray;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject convertRowToJsonObject(Row row,
                                                     List<String> cols) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i<cols.size(); i++) {
            jsonObject.put(cols.get(i), getCellValueAsString(row.getCell(i)));
        }
        return jsonObject;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null){
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
