package it.diegorigo.strings;

import org.apache.commons.text.CaseUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtils {
    public static final String REGEX_STRANGE_CHARACTERS = "[\u200B\u200C\u200D\uFEFF]";
    public static String capitalize(String text) {
        return org.apache.commons.lang3.StringUtils.capitalize(text);
    }

    public static String toCamelCase(String string) {
        return CaseUtils.toCamelCase(string, true, ' ');
    }

    public static String leftPad(String value,
                                 String character,
                                 int n) {
        return String.format("%" + character + n + "d", Integer.parseInt(value));
    }

    public static String leftPad(Long value,
                                 String character,
                                 int n) {
        return String.format("%" + character + n + "d", value);
    }

    public static String leftPad(Integer value,
                                 String character,
                                 int n) {
        return String.format("%" + character + n + "d", value);
    }

    public static String leftPadSpaces(Object value,
                                       int n) {
        return String.format("%" + n + "s", value);
    }

    public static boolean containsIgnoreCase(List<String> values,
                                             String value) {
        return values.stream().map(String::toLowerCase).toList().contains(value.toLowerCase());
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static String emptyString(int i) {
        return IntStream.range(0, i).mapToObj(v -> " ").collect(Collectors.joining());
    }

    public static String cleanString(String string){
       return string.replaceAll(REGEX_STRANGE_CHARACTERS, "");
    }
}
