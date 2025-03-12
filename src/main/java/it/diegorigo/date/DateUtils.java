package it.diegorigo.date;

import it.diegorigo.strings.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        return isoDate(today());
    }

    public static String isoDate(LocalDate date){
        return formattedDate(date,"yyyy-MM-dd");
    }

    public static String isoDateTime(){
        return formattedDateTime(now(),"yyyyMMddHHmm");
    }

    public static LocalDateTime now(){
        return LocalDateTime.now();
    }

    public static String getTimestampAsString() {
        return now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public static String formattedDate(LocalDate date, String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formattedDateTime(LocalDateTime date, String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getFormattedTime(LocalTime datetime) {
        return  DateTimeFormatter.ofPattern("HH:mm").format(datetime);
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

    public static String getShortYear() {
        return getYear().substring(2, 4);
    }

    public static String getShortYear(String year) {
        return year.substring(2, 4);
    }

}
