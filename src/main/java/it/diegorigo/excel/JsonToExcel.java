package it.diegorigo.excel;

import it.diegorigo.date.DateUtils;
import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class JsonToExcel {

    public static void main(String[] args) {
        try {
            toExcel(Paths.get("C:/creazioni.json"));
        } catch (UtilityException e) {
            throw new RuntimeException(e);
        }
    }

    public static void toExcel(Path path) throws UtilityException {
        JSONArray jsonArray = new JSONArray(FilesUtils.fileAsString(path));
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

            String fileName = String.join("_", DateUtils.isoDateTime(), FilesUtils.filenameWithoutExtension(path.toFile()) + ".xlsx");
            File file = Paths.get("C:/Output", fileName).toFile();
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                throw new UtilityException("Errore durante il salvataggio del file");
            }
        } catch (IOException e) {
            throw new UtilityException("Errore durante la conversione del file");
        }

    }

    public static void toJson(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("input.xlsx")) {
            // Caricamento del workbook
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            // Recupero del foglio desiderato (assume che ci sia solo un foglio nel file Excel)
            Sheet sheet = workbook.getSheetAt(0);

            // Creazione di un array JSON
            JSONArray jsonArray = new JSONArray();

            // Scorrimento delle righe
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                JSONObject jsonObject = new JSONObject();

                // Scorrimento delle colonne
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    // Usa il valore della cella come chiave nel JSON
                    String key = sheet.getRow(0)
                                      .getCell(cell.getColumnIndex())
                                      .getStringCellValue();
                    String value = getCellValueAsString(cell);

                    jsonObject.put(key, value);
                }

                // Aggiungi l'oggetto JSON all'array
                jsonArray.put(jsonObject);
            }

            // Stampa l'array JSON
            System.out.println(jsonArray.toString(2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
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
