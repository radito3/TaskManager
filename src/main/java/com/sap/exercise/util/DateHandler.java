package com.sap.exercise.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DateHandler {

    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy", "yyyy-MM-dd" };

    private DateHandler startDate;
    private DateHandler endDate;
    private final Calendar today = Calendar.getInstance();
    private Calendar currentCal = new GregorianCalendar();

    public enum Dates {
        TODAY(cal -> {}),
        IN_ONE_WEEK(cal -> cal.add(Calendar.DAY_OF_MONTH, 6));

        private Consumer<Calendar> cons;

        Dates(Consumer<Calendar> cons) {
            this.cons = cons;
        }
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
        dates.cons.accept(this.currentCal);
    }

    public Calendar asCalendar() {
        return currentCal;
    }

    public String asString() {
        return String.format("%d-%d-%d",
                currentCal.get(Calendar.YEAR),
                (currentCal.get(Calendar.MONTH) + 1),
                currentCal.get(Calendar.DAY_OF_MONTH));
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
        return fromTo().stream().anyMatch(date -> DateUtils.isSameDay(date, today));
    }
}
