package it.diegorigo.strings;

import org.apache.commons.text.CaseUtils;

public class StringUtils {
    public static String capitalize(String text){
        return  org.apache.commons.lang3.StringUtils.capitalize(text);
    }

    public static String toCamelCase(String string) {
        return CaseUtils.toCamelCase(string, true, ' ');
    }

    public static String leftPad(int value,
                                 String character,
                                 int n) {
        return String.format("%" + character + n + "d", value);
    }
}
