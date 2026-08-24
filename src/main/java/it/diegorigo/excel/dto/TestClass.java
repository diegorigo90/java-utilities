package it.diegorigo.excel.dto;

import it.diegorigo.excel.annotations.ExcelInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestClass {
    @ExcelInfo(position = 1, title = "Nome")
    private String nome;
    @ExcelInfo(position = 2, title = "Cognome")
    private String cognome;
    @ExcelInfo(position = 3, title = "Età")
    private Integer eta;
}
