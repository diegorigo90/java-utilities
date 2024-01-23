package it.diegorigo.date;

import it.diegorigo.strings.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String italianDate() {
        return today().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String getYear() {
        return String.valueOf(today().getYear());
    }

    public static String getMonth() {
        return today().getMonth().getDisplayName(TextStyle.FULL, Locale.ITALY);
    }

    public static String getMonthNumber() {
        return StringUtils.leftPad(today().getMonthValue(), "0", 2);
    }

}
