package it.diegorigo.excel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExcelPageInfo<T> {
    private String pageName;
    private List<T> content;
}
