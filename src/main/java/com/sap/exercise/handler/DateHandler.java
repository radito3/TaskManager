package com.sap.exercise.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateHandler {

    private static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy" };

    private Calendar currentCal = new Calendar.Builder().setCalendarType("gregorian").build();

    public DateHandler(String text) {
        try {
            String argument = StringUtils.removeEnd(text.trim(), "-");
            currentCal.setTime(DateUtils.parseDateStrictly(argument, DATE_FORMATS));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String asString(boolean start, boolean flag) {
        return String.valueOf(currentCal.get(Calendar.YEAR)) +
                '-' +
                (currentCal.get(Calendar.MONTH) + 1) +
                '-' +
                currentCal.get(Calendar.DAY_OF_MONTH) +
                (flag ? "" : (start ? " 00:00" : " 23:59"));
    }

    public String asString() {
        return this.asString(false, true);
    }

    public String asString(boolean start) {
        return this.asString(start, false);
    }

    public Calendar asCalendar() {
        return currentCal;
    }

    public static List<String> fromTo(String from, String to) {
        //increment date value by day
        return new ArrayList<>();
    }

    public static String stringifyDate(int year, int month, int day) {
        return Stream.of(year, month, day)
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
    }

    public static int[] inOneWeek(String day, String month, String year) {
        return getTime(Integer.valueOf(day) + 6, Integer.valueOf(month), Integer.valueOf(year));
    }

    public static int[] getToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR),
                month = cal.get(Calendar.MONTH) + 1,
                day = cal.get(Calendar.DAY_OF_MONTH);
        return new int[] { day, month, year };
    }

    public static int[] getTime(int day, int month, int year) {
        if (month > 12) {
            return getTime(day, 1, year + 1);
        }
        if (day > getMonthDays()[month]) {
            return getTime(day - getMonthDays()[month], month + 1, year);
        }
        return new int[] { day, month, year };
    }

    public static int[] getMonthDays() {
        return new int[] { 0, 31, isLeapYear() ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    }

    private static boolean isLeapYear(/*int year*/) {
        Calendar cal = Calendar.getInstance();
        /*cal.set(Calendar.YEAR, year);  --- for argument function */
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }
}
