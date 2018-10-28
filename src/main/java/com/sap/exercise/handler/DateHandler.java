package com.sap.exercise.handler;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DateHandler {

    private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final Map<String, SimpleDateFormat> dateFormats = new ConcurrentHashMap<>();
    static {
        dateFormats.put("\\s*\\d{1,2}-\\d{1,2}-2\\d{3} \\d{1,2}:\\d{1,2}:\\d{1,2}-?\\s*", DEFAULT_FORMAT);
        dateFormats.put("\\s*\\d{1,2}-\\d{1,2}-2\\d{3}-?\\s*", new SimpleDateFormat("dd-MM-yyyy"));
        dateFormats.put("\\s*\\d{1,2}/\\d{1,2}/2\\d{3} \\d{1,2}:\\d{1,2}-?\\s*", new SimpleDateFormat("dd/MM/yyyy HH:mm"));
        dateFormats.put("\\s*\\d{1,2}/\\d{1,2}/2\\d{3}-?\\s*", new SimpleDateFormat("dd/MM/yyyy"));
        dateFormats.put("\\s*\\d{1,2}.\\d{1,2}.2\\d{3} \\d{1,2}:\\d{1,2}-?\\s*", new SimpleDateFormat("dd.MM.yyyy HH:mm"));
        dateFormats.put("\\s*\\d{1,2}.\\d{1,2}.2\\d{3}-?\\s*", new SimpleDateFormat("dd.MM.yyyy"));
        dateFormats.put("\\s*\\d{1,2} (\\d{1,2}|[a-zA-Z]{3}) 2\\d{3} \\d{1,2}:\\d{1,2}-?\\s*", new SimpleDateFormat("dd MMM yyyy HH:mm"));
        dateFormats.put("\\s*\\d{1,2} (\\d{1,2}|[a-zA-Z]{3}) 2\\d{3}-?\\s*", new SimpleDateFormat("dd MMM yyyy"));
    }
    private Calendar currentCal = Calendar.getInstance();
    private SimpleDateFormat currentFormat;

    public DateHandler(String text) {
        this.findFormat(text);
        try {
            currentCal.setTime(currentFormat.parse(StringUtils.removeEnd(text.trim(), "-")));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unparseable date");  //TODO figure out why only dd.MM.yyyy and dd MMM yyyy work
        }
    }

    private String asString(boolean flag, boolean start, boolean flag2) {
        Date date = new Date(currentCal);
        String hours = currentFormat.toPattern().contains("HH") ?
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

    public Calendar asCalendar() {
        try {
            currentCal.setTime(DEFAULT_FORMAT.parse(this.asString()));
        } catch (ParseException ignored) {}
        return currentCal;
    }

    private void findFormat(String input) {
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
