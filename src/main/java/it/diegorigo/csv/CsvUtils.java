package it.diegorigo.csv;

import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.strings.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvUtils {

    public static List<CsvColumnData> toColumns(File csvFile) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        return toColumns(br);
    }

    public static List<CsvColumnData> toColumns(byte[] fileBytes) throws UtilityException {
        BufferedReader reader = toBufferedReader(fileBytes);
        return toColumns(reader);
    }

    private static BufferedReader toBufferedReader(byte[] fileBytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
        InputStreamReader isr = new InputStreamReader(bais, StandardCharsets.UTF_8);
        return new BufferedReader(isr);
    }

    public static List<CsvColumnData> toColumns(BufferedReader br) throws UtilityException {

        List<CsvColumnData> list = new ArrayList<>();
        try {

            String headerLine = br.readLine();
            if (headerLine == null)
                return list;

            String delimiter = detectSeparator(headerLine);

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

    public static String detectSeparator(String line) throws IOException {
        String[] possibleSeparators = {",", ";", "\t", "|"};
        Map<String, Integer> separatorScores = new HashMap<>();
        for (String sep : possibleSeparators) {
            int count = countOccurrences(line, sep);
            separatorScores.put(sep, separatorScores.getOrDefault(sep, 0) + count);
        }
        return separatorScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(",");
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

    public static List<CsvRow> parseCsv(byte[] fileBytes) throws IOException {

        List<CsvRow> rows = new ArrayList<>();
        String line;
        String[] headers = null;

        try (BufferedReader br = toBufferedReader(fileBytes)) {
            line = br.readLine();
            String separator = detectSeparator(line);
            if (line != null) {
                headers = StringUtils.cleanString(line).split(separator);
            }

            if (headers == null) {
                throw new IOException("Il file è vuoto o l'intestazione è mancante.");
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
                LinkedHashMap<String, String> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    rowMap.put(headers[i].trim(), values[i].trim());
                }
                rows.add(new CsvRow(rowMap));
            }
        }

        return rows;
    }

}
