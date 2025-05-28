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

        List<CsvColumnData> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            String headerLine = br.readLine();
            if (headerLine == null)
                return list;

            String delimiter = detectSeparator(csvFile);

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

    public static String detectSeparator(File csvFile) throws IOException {
        String[] possibleSeparators = {",", ";", "\t", "|"};
        int linesToCheck = 5;

        Map<String, Integer> separatorScores = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int linesRead = 0;

            while ((line = reader.readLine()) != null && linesRead < linesToCheck) {
                for (String sep : possibleSeparators) {
                    int count = countOccurrences(line, sep);
                    separatorScores.put(sep, separatorScores.getOrDefault(sep, 0) + count);
                }
                linesRead++;
            }
        }

        return separatorScores.entrySet()
                              .stream()
                              .max(Map.Entry.comparingByValue())
                              .map(Map.Entry::getKey)
                              .orElse(",");
    }

    private static int countOccurrences(String line,
                                        String separator) {
        int count = 0;
        for (char ch : line.toCharArray()) {
            if (Character.toString(ch).equals(separator)) {
                count++;
            }
        }
        return count;
    }

}
