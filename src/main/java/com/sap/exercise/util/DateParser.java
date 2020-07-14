package com.sap.exercise.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DateParser {

    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy", "yyyy-MM-dd" };

    private Calendar currentCal = Calendar.getInstance();

    public DateParser(String text) {
        try {
            String argument = StringUtils.removeEnd(text, "-").trim();
            currentCal.setTime(DateUtils.parseDateStrictly(argument, DATE_FORMATS));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public DateParser() {
    }

    public Calendar asCalendar() {
        return currentCal;
    }

    void addOneWeek() {
        currentCal = (Calendar) currentCal.clone();
        currentCal.add(Calendar.DAY_OF_MONTH, 6);
    }

    public String asString() {
        return String.format("%1$tY-%1$tm-%1$td", currentCal);
    }

    public static List<SimplifiedCalendar> getRangeBetween(String start, String end) {
        DateParser startDate = new DateParser(start);
        DateParser endDate = new DateParser(end);

        long days = TimeUnit.MILLISECONDS.toDays(endDate.currentCal.getTimeInMillis() - startDate.currentCal.getTimeInMillis());

        return LongStream.rangeClosed(0, days)
                         .mapToObj(i -> {
                             Calendar cal = (Calendar) startDate.currentCal.clone();
                             cal.add(Calendar.DAY_OF_MONTH, Math.toIntExact(i));
                             return new SimplifiedCalendar(cal);
                         })
                         .collect(Collectors.toList());
    }
}
