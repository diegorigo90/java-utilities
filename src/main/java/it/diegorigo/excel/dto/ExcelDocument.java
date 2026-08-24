/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel.dto;

import it.diegorigo.excel.enums.CellStyleType;
import it.diegorigo.excel.enums.ExcelType;
import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelDocument extends ExcelInfo implements AutoCloseable {

    private OdfDocument document;

    private Workbook workbook;

    private Map<CellStyleType, CellStyle> styles = new HashMap<>();

    public ExcelDocument(ExcelType excelType) throws Exception {
        this.excelType = excelType;
        switch (excelType) {
            case XLSX:
                workbook = new XSSFWorkbook();
                break;
            case ODS:
                document = OdfSpreadsheetDocument.newSpreadsheetDocument();
                break;
        }
    }

    public ExcelDocument(String path) throws UtilityException {
        this(Paths.get(path).toFile());
    }

    public ExcelDocument(File file) throws UtilityException {
        super();
        String extension = FilesUtils.getFileExtension(file);
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            switch (extension) {
                case "xlsx":
                    this.excelType = ExcelType.XLSX;
                    workbook = new XSSFWorkbook(fileInputStream);
                    createBaseStyles(workbook);
                    break;
                case "ods":
                    this.excelType = ExcelType.ODS;
                    document = OdfDocument.loadDocument(file);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "The selected file is not currently supported");
            }
        } catch (Exception e) {
            throw new UtilityException("Error handling Excel file", e);
        }
    }

    private void createBaseStyles(Workbook workbook) {
        createHeaderStyle(workbook);
        createHyperlinkStyle(workbook);
    }

    private void createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 15);
        headerStyle.setFont(font);
        styles.put(CellStyleType.HEADER, headerStyle);
    }

    private void createHyperlinkStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        styles.put(CellStyleType.HYPERLINK, style);
    }

    public ExcelSheet createSheet(String sheetname) {
        return switch (excelType) {
            case ODS -> {
                OdfTable table;
               if ( isEmptyDocument()){
                   table = document.getTableList(true).get(0);
               } else {
                   table = OdfTable.newTable(document,1,1);
               }
                table.setTableName(sheetname);
                yield new ExcelSheet(table);
            }
            case XLSX -> new ExcelSheet(workbook.createSheet(sheetname));
        };
    }

    private boolean isEmptyDocument(){
        List<OdfTable> tableList = document.getTableList(true);
        if (tableList.size() == 1) {
            OdfTable firstTable = tableList.get(0);
            List<OdfTableRow> rows = firstTable.getRowList();
            if (rows.size() == 1) {
                OdfTableRow firstRow = rows.get(0);
                int cellCount = firstRow.getCellCount();
                boolean empty = true;
                for (int i = 0; i <cellCount; i++) {
                    OdfTableCell cell = firstRow.getCellByIndex(i);
                    if (!cell.getStringValue().isEmpty()){
                        empty = false;
                    }
                }
                return empty;
            }
        }

        return false;
    }

    public ExcelSheet getSheetByIndex(int index) {
        return switch (excelType) {
            case ODS -> new ExcelSheet(document.getTableList(true).get(index));
            case XLSX -> new ExcelSheet(workbook.getSheetAt(index));
        };
    }

    public ExcelSheet getSheet(String sheetName) {
        return switch (excelType) {
            case ODS -> new ExcelSheet(document.getTableByName(sheetName));
            case XLSX -> new ExcelSheet(workbook.getSheet(sheetName));
        };
    }

    public void save(FileOutputStream fileOut) throws UtilityException {
        try {
            switch (excelType) {
                case ODS -> document.save(fileOut);
                case XLSX -> workbook.write(fileOut);
            }
        } catch (Exception e) {
            throw new UtilityException("Errore durante il salvataggio del file", e);
        }
    }

    public void save(ByteArrayOutputStream bos) {
        try {
            switch (excelType) {
                case ODS -> document.save(bos);
                case XLSX -> workbook.write(bos);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(File file) throws UtilityException {
        try {
            switch (excelType) {
                case ODS -> {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        document.save(fos);
                    } catch (Exception e) {
                        throw new UtilityException("Errore durante il salvataggio del file", e);
                    }
                }
                case XLSX -> {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        workbook.write(fos);
                    } catch (Exception e) {
                        throw new UtilityException("Errore durante il salvataggio del file", e);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void close() {
        try {
            switch (excelType) {
                case ODS -> document.close();
                case XLSX -> workbook.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void open(File file) throws UtilityException {
        FilesUtils.openFile(file);
    }

    public byte[] toByteArray() throws UtilityException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            save(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new UtilityException("Error during workbook conversion", e);
        }
    }
}
