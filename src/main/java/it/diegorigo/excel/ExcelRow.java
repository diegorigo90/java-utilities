/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel;

import it.diegorigo.exceptions.UtilityException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

import java.util.List;

public class ExcelRow extends ExcelInfo {
    private OdfTableRow odfTableRow;

    private Row row;

    public ExcelRow(OdfTableRow odfTableRow) {
        excelType = ExcelType.ODS;
        this.odfTableRow = odfTableRow;
    }

    public ExcelRow(Row row) {

        excelType = ExcelType.XLSX;
        this.row = row;
    }

    public ExcelCell getCell(int n) {
        return switch (excelType) {
            case ODS -> new ExcelCell(odfTableRow.getCellByIndex(n));
            case XLSX -> new ExcelCell(row.getCell(n));
        };
    }

    public void writeFlatRow(List<String> values) throws UtilityException {
        switch (excelType) {
            case ODS -> throw new UtilityException("Funzione non ancora supportata");
            case XLSX -> {
                for (String value : values) {
                    Cell cell = row.createCell(Math.max(0, row.getLastCellNum()));
                    cell.setCellValue(value);
                }
            }
        }
    }

    public void writeFlatRow(List<String> values, CellStyle style) throws UtilityException {
        switch (excelType) {
            case ODS -> throw new UtilityException("Funzione non ancora supportata");
            case XLSX -> {
                for (String value : values) {
                    Cell cell = row.createCell(Math.max(0, row.getLastCellNum()));
                    cell.setCellStyle(style);
                    cell.setCellValue(value);
                }
            }
        }
    }
}
