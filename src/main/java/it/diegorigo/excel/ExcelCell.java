/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

public class ExcelCell extends ExcelInfo {

    private OdfTableCell odfTableCell;

    private Cell cell;

    public ExcelCell(OdfTableCell odfTableCell) {
        excelType = ExcelType.ODS;
        this.odfTableCell = odfTableCell;
    }

    public ExcelCell(Cell cell) {
        excelType = ExcelType.XLSX;
        this.cell = cell;
    }

    public String getStringValue() {
        return switch (excelType) {
            case ODS -> odfTableCell.getStringValue();
            case XLSX -> cell.getStringCellValue();
        };
    }

    public void setStringValue(String value) {
        switch (excelType) {
            case ODS -> odfTableCell.setStringValue(value);
            case XLSX -> cell.setCellValue(value);
        }
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        switch (excelType) {
            case ODS -> odfTableCell.setHorizontalAlignment(horizontalAlignment.toString());
            case XLSX ->
                    cell.getCellStyle().setAlignment(convertToPoiAlignment(horizontalAlignment));
        }
    }

    private org.apache.poi.ss.usermodel.HorizontalAlignment convertToPoiAlignment(HorizontalAlignment horizontalAlignment) {
        return switch (horizontalAlignment) {
            case LEFT -> org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT;
            case RIGHT -> org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT;
            case CENTER -> org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
        };
    }

    public Double getNumericValue() {
        return switch (excelType) {
            case ODS -> odfTableCell.getDoubleValue();
            case XLSX -> cell.getNumericCellValue();
        };
    }

    public int getIntegerValue() {
        return switch (excelType) {
            case ODS -> getIntegerValue(odfTableCell.getDoubleValue());
            case XLSX -> getIntegerValue(cell.getNumericCellValue());
        };
    }

    public static int getIntegerValue(Double doubleValue) {
        try {
            return doubleValue.intValue();
        } catch (Exception e) {
            return -1;
        }
    }
}
