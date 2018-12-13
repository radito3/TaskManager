package com.sap.exercise.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DateHandler {

    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy", "yyyy-MM-dd" };

    private Calendar currentCal = Calendar.getInstance();
    private DateHandler startDate;
    private DateHandler endDate;

    public enum Dates {
        TODAY, IN_ONE_WEEK
    }

    public DateHandler(String text) {
        try {
            String argument = StringUtils.removeEnd(text.trim(), "-");
            this.currentCal.setTime(DateUtils.parseDateStrictly(argument, DATE_FORMATS));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public DateHandler(String start, String end) {
        this.startDate = new DateHandler(start);
        this.endDate = new DateHandler(end);
    }

    public DateHandler(Calendar calendar) {
        this.currentCal = calendar;
    }

    public DateHandler(Dates dates) {
        currentCal = Calendar.getInstance();
        switch (dates) {
            case TODAY:
                break;
            case IN_ONE_WEEK:
                currentCal.add(Calendar.DAY_OF_MONTH, 6);
                break;
        }
    }

    public Calendar asCalendar() {
        return currentCal;
    }

    public String asString() {
        return String.valueOf(currentCal.get(Calendar.YEAR)) +
                '-' +
                (currentCal.get(Calendar.MONTH) + 1) +
                '-' +
                currentCal.get(Calendar.DAY_OF_MONTH);
    }

    public List<Calendar> fromTo() {
        if (startDate == null || endDate == null) {
            throw new UnsupportedOperationException();
        }

        long days = (endDate.currentCal.getTimeInMillis() - startDate.currentCal.getTimeInMillis()) / DateUtils.MILLIS_PER_DAY;

        return LongStream.rangeClosed(0, days)
                .mapToObj(i -> {
                    Calendar cal = (Calendar) startDate.currentCal.clone();
                    cal.add(Calendar.DAY_OF_MONTH, Math.toIntExact(i));
                    return cal;
                })
                .collect(Collectors.toList());
    }

    public boolean containsToday() {
        if (startDate == null || endDate == null) {
            throw new UnsupportedOperationException();
        }

        Calendar today = Calendar.getInstance();
        return startDate.currentCal.get(Calendar.DAY_OF_MONTH) <= today.get(Calendar.DAY_OF_MONTH) &&
                endDate.currentCal.get(Calendar.DAY_OF_MONTH) >= today.get(Calendar.DAY_OF_MONTH);
    }
}
