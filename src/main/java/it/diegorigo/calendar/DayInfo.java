package it.diegorigo.calendar;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DayInfo {
    private Date date;
    private String dayOfWeek;
    private Integer dayOfMonth;
    private String month;
    private Integer week;
    private Integer yearWeek;

    public DayInfo() {
    }

    public DayInfo(Calendar calendar) {
        this.date = calendar.getTime();
        this.week = calendar.get(Calendar.WEEK_OF_MONTH);
        this.month = StringUtils.capitalize(calendar.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault()));
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        this.dayOfWeek = StringUtils.capitalize(calendar.getDisplayName(
                Calendar.DAY_OF_WEEK,
                Calendar.LONG,
                Locale.getDefault()));
        this.yearWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (this.yearWeek > 40 && calendar.get(Calendar.MONTH) == 0) {
            this.yearWeek = 0;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getYearWeek() {
        return yearWeek;
    }

    public void setYearWeek(Integer yearWeek) {
        this.yearWeek = yearWeek;
    }

    @Override
    public String toString() {
        return "DayInfo{" + "date=" + date + ", dayOfWeek='" + dayOfWeek + '\'' + ", dayOfMonth=" + dayOfMonth + ", month='" + month + '\'' + ", week=" + week + ", yearWeek=" + yearWeek + '}';
    }
}
