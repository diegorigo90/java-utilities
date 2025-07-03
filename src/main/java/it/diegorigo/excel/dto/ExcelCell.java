/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel.dto;

import it.diegorigo.excel.enums.ExcelType;
import it.diegorigo.excel.enums.HorizontalAlignment;
import it.diegorigo.numbers.NumbersUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

import java.time.LocalTime;
import java.util.Calendar;

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

    public Double getNumericValue() {
        return switch (excelType) {
            case ODS -> odfTableCell.getDoubleValue();
            case XLSX -> cell.getNumericCellValue();
        };
    }

    public void setNumericValue(double value) {
        switch (excelType) {
            case ODS -> odfTableCell.setDoubleValue((double) value);
            case XLSX -> cell.setCellValue(value);
        }
    }

    public int getIntegerValue() {
        return switch (excelType) {
            case ODS -> NumbersUtils.getIntegerValue(odfTableCell.getDoubleValue());
            case XLSX -> NumbersUtils.getIntegerValue(cell.getNumericCellValue());
        };
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        switch (excelType) {
            case ODS -> odfTableCell.setHorizontalAlignment(horizontalAlignment.toString().toLowerCase());
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
}
