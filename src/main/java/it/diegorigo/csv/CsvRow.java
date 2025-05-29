package it.diegorigo.csv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CsvRow {
    LinkedHashMap<String, String> values  = new LinkedHashMap<>();
}
