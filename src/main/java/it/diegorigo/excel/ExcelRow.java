/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.excel;

import org.apache.poi.ss.usermodel.Row;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;

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
}
