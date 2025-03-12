/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel.dto;

import it.diegorigo.excel.enums.ExcelType;
import it.diegorigo.exceptions.UtilityException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelSheet extends ExcelInfo {

    private OdfTable table;

    private Sheet sheet;

    public ExcelSheet(OdfTable table) {
        excelType = ExcelType.ODS;
        this.table = table;
    }

    public ExcelSheet(Sheet sheet) {
        excelType = ExcelType.XLSX;
        this.sheet = sheet;
    }

    public ExcelRow getRow(int n) {
        return switch (excelType) {
            case ODS -> new ExcelRow(table.getRowByIndex(n));
            case XLSX -> new ExcelRow(sheet.getRow(n));
        };
    }

    public List<ExcelRow> getRowList() {
        return switch (excelType) {
            case ODS -> table.getRowList().stream().map(ExcelRow::new).toList();
            case XLSX -> {
                Iterator<Row> iterator = sheet.rowIterator();
                List<Row> rows = new ArrayList<>();
                iterator.forEachRemaining(rows::add);
                yield rows.stream().map(ExcelRow::new).toList();
            }
        };
    }

    public void deleteRow(int index) {
        switch (excelType) {
            case ODS -> table.removeRowsByIndex(index, index);
            case XLSX -> sheet.removeRow(sheet.getRow(index));
        }
    }

    public ExcelRow createRow() {
        return switch (excelType) {
            case ODS -> new ExcelRow(table.appendRow()) ;
            case XLSX -> new ExcelRow(sheet.createRow(sheet.getLastRowNum()+1));
        };
    }

    public ExcelRow createRow(int rownum) {
        return switch (excelType) {
            case ODS -> {
                List<OdfTableRow> rows = table.insertRowsBefore(rownum + 1, 1);
                yield new ExcelRow(rows.get(0));
            }
            case XLSX -> new ExcelRow(sheet.createRow(rownum));
        };
    }

    public void autosizeColumns(int numberColumns) throws UtilityException {
        switch (excelType) {
            case ODS -> throw new UtilityException("Funzione non ancora supportata");
            case XLSX -> {
                for (int i = 0; i < numberColumns; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
        }
        ;

    }
}
