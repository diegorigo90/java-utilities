package it.diegorigo.calendar;

import it.diegorigo.strings.StringUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarUtils {
    public static List<DayInfo> getYearDays(String anno) {
        int yearDecimal = Integer.parseInt(anno);
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearDecimal, Calendar.JANUARY, 1);
        int lastDayOfYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        boolean END = false;
        List<DayInfo> days = new ArrayList<>();

        while (!END) {
            days.add(new DayInfo(calendar));
            if (calendar.get(Calendar.DAY_OF_YEAR) == lastDayOfYear)
                END = true;
            calendar.add(Calendar.DATE, 1);
        }

        return days;
    }

    public static List<String> getMonths() {
        return Arrays.stream(Month.values())
                     .map(item -> StringUtils.capitalize(item.getDisplayName(
                             TextStyle.FULL,
                             Locale.getDefault())))
                     .collect(Collectors.toList());
    }

    public static List<String> getDaysOfWeek() {
        return Arrays.stream(DayOfWeek.values())
                     .map(item -> StringUtils.capitalize(item.getDisplayName(
                             TextStyle.FULL,
                             Locale.getDefault())))
                     .collect(Collectors.toList());
    }

    public static boolean isBisestile(String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public static String now() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
