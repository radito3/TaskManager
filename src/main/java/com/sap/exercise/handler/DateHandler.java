package com.sap.exercise.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DateHandler {

    private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final Map<String, SimpleDateFormat> dateFormats = new ConcurrentHashMap<>();
    {
        dateFormats.put("(\\d{1,2})-\\1-2\\d{3} \\1:\\1:\\1", DEFAULT_FORMAT);
        dateFormats.put("\\d{1,2}-\\d{1,2}-2\\d{3}", new SimpleDateFormat("dd-MM-yyyy"));
        dateFormats.put("\\d{1,2}/\\d{1,2}/2\\d{3} \\d{1,2}:\\d{1,2}:\\d{1,2}", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
        dateFormats.put("\\d{1,2}/\\d{1,2}/2\\d{3}", new SimpleDateFormat("dd/MM/yyyy"));
        dateFormats.put("\\d{1,2}.\\d{1,2}.2\\d{3} \\d{1,2}:\\d{1,2}:\\d{1,2}", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
        dateFormats.put("\\d{1,2}.\\d{1,2}.2\\d{3}", new SimpleDateFormat("dd.MM.yyyy"));
        dateFormats.put("\\d{1,2} (\\d{1,2}|[a-zA-Z]{3}) 2\\d{3}", new SimpleDateFormat("dd MMM yyyy HH:mm:ss"));
        //TODO add remaining time formats
    }
    private Calendar current = Calendar.getInstance();
    private SimpleDateFormat currentFormat;
    private String input;

    public DateHandler(String text) {
        this.input = text;
        this.findFormat();
    }

    private String asString(boolean flag, boolean start, boolean flag2) {
        Date date = new Date(current);
        String hours = getPattern().contains("HH") ?
                date.hour + ":" + date.minute + ":" + date.second :
                flag ? (start ? "00:00:00" : "23:59:59") : "12:00:00";

        String format = "%d-" + date.month + "-%d " + hours;

        return flag2 ? String.format(format, date.day, date.year) : String.format(format, date.year, date.day);
    }

    public String asString() {
        return asString(false, false, true);
    }

    public String asString(boolean start) {
        return asString(true, start, false);
    }

    public String getPattern() {
        return currentFormat.toPattern();
    }

    public Calendar asCalendar() {
        try {
            current.setTime(currentFormat.parse(input));
        } catch (ParseException ignored) {}
        return current;
    }

    private void findFormat() {
        for (Map.Entry<String, SimpleDateFormat> entry : dateFormats.entrySet()) {
            if (input.matches(entry.getKey())) {
                currentFormat = entry.getValue();
                return;
            }
        }
        throw new IllegalArgumentException("Invalid date format");
    }

    private static class Date {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;

        Date(Calendar cal) {
            this.year = cal.get(Calendar.YEAR);
            this.month = cal.get(Calendar.MONTH) + 1;
            this.day = cal.get(Calendar.DAY_OF_MONTH);
            this.hour = cal.get(Calendar.HOUR_OF_DAY);
            this.minute = cal.get(Calendar.MINUTE);
            this.second = cal.get(Calendar.SECOND);
        }

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
