package it.diegorigo.date;

import it.diegorigo.strings.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class DateUtilsTest {

    @Test
    void todayTest() {
        LocalDate today = LocalDate.now();
        LocalDate result = DateUtils.today();

        Assertions.assertEquals(today.getYear(), result.getYear());
        Assertions.assertEquals(today.getMonth(), result.getMonth());
        Assertions.assertEquals(today.getDayOfMonth(), result.getDayOfMonth());
    }

    @Test
    void italianDateTest() {
        LocalDate today = LocalDate.now();
        String result = DateUtils.italianDate();

        Assertions.assertEquals(Integer.toString(today.getDayOfMonth()), result.substring(0,2));
        Assertions.assertEquals(StringUtils.leftPad(today.getMonthValue(),"0",2), result.substring(3,5));
        Assertions.assertEquals(Integer.toString(today.getYear()), result.substring(6));
    }

    @Test
    void getYearTest() {
        String result = DateUtils.getYear();

        Assertions.assertEquals(Integer.toString(DateUtils.today().getYear()), result);
    }

    @Test
    void getMonthTest() {
        String result = DateUtils.getMonth();

        Assertions.assertEquals(DateUtils.today().getMonth().getDisplayName(TextStyle.FULL, Locale.ITALY), result);
    }

    @Test
    void getMonthNumberTest() {
        String result = DateUtils.getMonthNumber();

        Assertions.assertEquals(StringUtils.leftPad(DateUtils.today().getMonthValue(), "0", 2), result);
    }
}
