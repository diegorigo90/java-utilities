package it.diegorigo.strings;

import org.apache.commons.text.CaseUtils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static String capitalize(String text){
        return  org.apache.commons.lang3.StringUtils.capitalize(text);
    }

    public static String toCamelCase(String string) {
        return CaseUtils.toCamelCase(string, true, ' ');
    }

    public static String leftPad(Object value,
                                 String character,
                                 int n) {
        return String.format("%" + character + n + "d", value);
    }

    public static boolean containsIgnoreCase(List<String> values,
                                             String value) {
        return values.stream().map(String::toLowerCase).toList().contains(value.toLowerCase());
    }
}
