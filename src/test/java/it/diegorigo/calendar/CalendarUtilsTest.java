package it.diegorigo.calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalendarUtilsTest {

    @Test
    void getYearDaysTest() {
        List<DayInfo> days = CalendarUtils.getYearDays("2020");

        Assertions.assertEquals(366, days.size());
        Map<String, List<DayInfo>> months = days.stream()
                                                .collect(Collectors.groupingBy(
                                                        DayInfo::getMonth));
        Assertions.assertEquals(12, months.size());
        months.forEach((month, monthDays) -> {
            Assertions.assertTrue(monthDays.size() >= 28);
            Assertions.assertTrue(monthDays.size() <= 31);
        });
    }

    @Test
    void getMonthsTest() {
        List<String> months = CalendarUtils.getMonths();
    }

    @Test
    void getDaysOfWeekTest() {
        List<String> daysOfWeek = CalendarUtils.getDaysOfWeek();
        Assertions.assertEquals(7,daysOfWeek.size());
    }

    @Test
    void isBisestileTrueTest() {
        boolean isBisestile = CalendarUtils.isBisestile("2020");
        Assertions.assertEquals(true,isBisestile);
    }

    @Test
    void isBisestileFalseTest() {
        boolean isBisestile = CalendarUtils.isBisestile("2022");
        Assertions.assertEquals(false,isBisestile);
    }

    @Test
    void nowTest() {
        String string = CalendarUtils.now();
        Assertions.assertNotNull(string);
    }
}
