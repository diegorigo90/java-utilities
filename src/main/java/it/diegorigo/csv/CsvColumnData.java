package it.diegorigo.csv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CsvColumnData {
    private String name;
    private List<String> values;
}
