package it.diegorigo.csv;

import it.diegorigo.exceptions.UtilityException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtils {
    public static List<CsvColumnData> toColumns(File csvFile) throws UtilityException {
        return toColumns(csvFile, ",");
    }

    public static List<CsvColumnData> toColumns(File csvFile,
                                                String delimiter) throws UtilityException {

        List<CsvColumnData> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            String headerLine = br.readLine();
            if (headerLine == null)
                return list;

            String[] headers = headerLine.split(delimiter);
            int size = headers.length;

            Map<Integer, List<String>> map = new HashMap<>();
            for (int i = 0; i < size; i++) {
                map.put(i, new ArrayList<>());
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter, -1);
                if (values.length != size) {
                    throw new UtilityException("Malformed row! -> " + line);
                }
                for (int i = 0; i < size; i++) {
                    map.get(i).add(values[i].trim());
                }
            }
            br.close();

            for (int i = 0; i < size; i++) {
                list.add(new CsvColumnData(headers[i].trim(), map.get(i)));
            }
        } catch (IOException e) {
            throw new UtilityException("Errore di lettura file CSV", e);
        }

        return list;
    }
}
