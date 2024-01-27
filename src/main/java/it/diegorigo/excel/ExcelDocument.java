/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel;

import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.odftoolkit.odfdom.doc.OdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ExcelDocument extends ExcelInfo implements AutoCloseable {

    private OdfDocument document;

    private Workbook workbook;

    private Map<CellStyleType, CellStyle> styles = new HashMap<>();

    public ExcelDocument(ExcelType excelType) {
        this.excelType = excelType;
    }

    public ExcelDocument(String path) throws UtilityException {
        this(Paths.get(path).toFile());
    }

    public ExcelDocument(File file) throws UtilityException {
        super();
        String extension = FilesUtils.getFileExtension(file);
        try {
            switch (extension) {
                case "xlsx":
                    this.excelType = ExcelType.XLSX;
                    workbook = new XSSFWorkbook(file);
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

    public void save(String filename) throws UtilityException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            workbook.write(fos);
        } catch (Exception e) {
            throw new UtilityException("Errore durante il salvataggio del file", e);
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
        FilesUtils.open(file);
    }


}
