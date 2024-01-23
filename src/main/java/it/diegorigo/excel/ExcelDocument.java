/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel;

import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.files.FilesUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.odftoolkit.odfdom.doc.OdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

public class ExcelDocument extends ExcelInfo implements AutoCloseable {

    private OdfDocument document;

    private Workbook workbook;

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

    public ExcelSheet getSheet(String sheetName) {
        return switch (excelType) {
            case ODS -> new ExcelSheet(document.getTableByName(sheetName));
            case XLSX -> new ExcelSheet(workbook.getSheet(sheetName));
        };
    }

    public void save(FileOutputStream fileOut) {
        try {
            switch (excelType) {
                case ODS -> document.save(fileOut);
                case XLSX -> workbook.write(fileOut);
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


}
