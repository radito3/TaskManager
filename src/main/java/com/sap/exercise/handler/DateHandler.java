package com.sap.exercise.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class DateHandler {

    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy", "yyyy-MM-dd" };

    private Calendar currentCal = Calendar.getInstance();

    public DateHandler(String text) {
        try {
            String argument = StringUtils.removeEnd(text.trim(), "-");
            currentCal.setTime(DateUtils.parseDateStrictly(argument, DATE_FORMATS));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    DateHandler(Calendar calendar) {
        this.currentCal = calendar;
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

    static List<Calendar> fromTo(String fromStr, String toStr) {
        Supplier<DateHandler> supplier = () -> new DateHandler(fromStr);
        if (fromStr.equals(toStr)) {
            return Collections.singletonList(supplier.get().currentCal);
        }

        DateHandler to = new DateHandler(toStr);
        long days = (to.currentCal.getTimeInMillis() - supplier.get().currentCal.getTimeInMillis()) / DateUtils.MILLIS_PER_DAY;

        return LongStream.rangeClosed(0, days)
                .mapToObj(i -> {
                    Calendar cal = supplier.get().currentCal;
                    cal.add(Calendar.DAY_OF_MONTH, Math.toIntExact(i));
                    return cal;
                })
                .collect(Collectors.toList());
    }

    static boolean containsToday(String start, String end) {
        Calendar from = new DateHandler(start).currentCal;
        Calendar to = new DateHandler(end).currentCal;
        Calendar today = Calendar.getInstance();
        return from.get(Calendar.DAY_OF_MONTH) <= today.get(Calendar.DAY_OF_MONTH) &&
                to.get(Calendar.DAY_OF_MONTH) >= today.get(Calendar.DAY_OF_MONTH);
    }

    public static String stringifyDate(int year, int month, int day) {
        return Stream.of(year, month, day)
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
    }

}
