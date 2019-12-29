package com.sap.exercise.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DateHandler {

    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy", "yyyy-MM-dd" };

    private DateHandler startDate;
    private DateHandler endDate;
    private Calendar currentCal = Calendar.getInstance();

    public DateHandler(String text) {
        try {
            String argument = StringUtils.removeEnd(text, "-").trim();
            currentCal.setTime(DateUtils.parseDateStrictly(argument, DATE_FORMATS));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public DateHandler(String start, String end) {
        startDate = new DateHandler(start);
        endDate = new DateHandler(end);
    }

    public DateHandler() {
    }

    public Calendar asCalendar() {
        return currentCal;
    }

    void addOneWeek() {
        currentCal = (Calendar) currentCal.clone();
        currentCal.add(Calendar.DAY_OF_MONTH, 6);
    }

    @Override
    public String toString() {
        return String.format("%1$tY-%1$tm-%1$td", currentCal);
    }

    public List<CalendarWrapper> fromTo() {
        if (startDate == null || endDate == null) {
            throw new UnsupportedOperationException();
        }

        long days = TimeUnit.MILLISECONDS.toDays(endDate.currentCal.getTimeInMillis() - startDate.currentCal.getTimeInMillis());

        return LongStream.rangeClosed(0, days)
                .mapToObj(i -> {
                    Calendar cal = (Calendar) startDate.currentCal.clone();
                    cal.add(Calendar.DAY_OF_MONTH, Math.toIntExact(i));
                    return new CalendarWrapper(cal);
                })
                .collect(Collectors.toList());
    }
}
