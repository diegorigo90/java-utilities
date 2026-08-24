package it.diegorigo.excel.dto;

import it.diegorigo.excel.annotations.ExcelInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestClass2 {
    @ExcelInfo(position = 1, title = "Colore")
    private String colore;
    @ExcelInfo(position = 2, title = "Quantità")
    private Integer quantita;
}
