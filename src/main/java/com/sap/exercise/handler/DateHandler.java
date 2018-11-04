package com.sap.exercise.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateHandler {

    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
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

    public String asString() {
        return String.valueOf(currentCal.get(Calendar.YEAR)) +
                '-' +
                (currentCal.get(Calendar.MONTH) + 1) +
                '-' +
                currentCal.get(Calendar.DAY_OF_MONTH);
    }

    public Calendar asCalendar() {
        return currentCal;
    }

    static List<String> fromTo(String from, String to) {
        if (from.equals(to)) {
            return Collections.singletonList(from);
        }
        String[] dateFrom = from.split("-");
        String[] dateTo = from.split("-");

        int[] fromDate = getSafeTime(Integer.valueOf(dateFrom[2]), Integer.valueOf(dateFrom[1]), Integer.valueOf(dateFrom[0]));
        int[] toDate = getSafeTime(Integer.valueOf(dateTo[2]), Integer.valueOf(dateTo[1]), Integer.valueOf(dateTo[0]));

        int toDays = toDate[0] - fromDate[0];
        for (int i = fromDate[1]; i < toDate[1]; i++) {
            toDays += getMonthDays()[i];
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; toDays > 0 && i <= toDays; i++) {
            result.add(dateToString(getSafeTime(fromDate[0] + i, fromDate[1], fromDate[2])));
        }
        return result;
    }

    static boolean containsToday(String start, String end) {
        return fromTo(start, end).contains(dateToString(getToday()));
    }

    private static String dateToString(int[] date) {
        return stringifyDate(date[2], date[1], date[0]);
    }

    public static String stringifyDate(int year, int month, int day) {
        return Stream.of(year, month, day)
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
    }

    public static int[] inOneWeek(String day, String month, String year) {
        return getSafeTime(Integer.valueOf(day) + 6, Integer.valueOf(month), Integer.valueOf(year));
    }

    public static int[] getToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR),
                month = cal.get(Calendar.MONTH) + 1,
                day = cal.get(Calendar.DAY_OF_MONTH);
        return new int[] { day, month, year };
    }

    public static int[] getSafeTime(int day, int month, int year) {
        if (month > 12) {
            return getSafeTime(day, 1, year + 1);
        }
        if (day > getMonthDays()[month]) {
            return getSafeTime(day - getMonthDays()[month], month + 1, year);
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
