package it.diegorigo.date;

import it.diegorigo.strings.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String italianDate() {
        return formattedDate(today(),"dd/MM/yyyy");
    }

    public static String isoDate(){
        return formattedDate(today(),"yyyy-MM-dd");
    }

    public static String isoDateTime(){
        return formattedDateTime(now(),"yyyyMMddHHmm");
    }

    public static LocalDateTime now(){
        return LocalDateTime.now();
    }

    public static String formattedDate(LocalDate date, String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formattedDateTime(LocalDateTime date, String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
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
