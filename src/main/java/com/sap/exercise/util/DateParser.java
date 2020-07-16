package com.sap.exercise.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DateParser {

    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy", "yyyy-MM-dd" };

    private LocalDateTime date;

    public DateParser(String text) {
        String argument = StringUtils.removeEnd(text, "-").trim();
        if (!tryParseArgument(argument)) {
            throw new IllegalArgumentException("Unsupported date format");
        }
    }

    private boolean tryParseArgument(String argument) {
        for (String format : DATE_FORMATS) {
            try {
                date = LocalDateTime.from(DateTimeFormatter.ofPattern(format).parse(argument));
                return true;
            } catch (DateTimeParseException ex) {
                //ignored
            }
        }
        return false;
    }

    public DateParser() {
        date = LocalDateTime.now();
    }

    private DateParser(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    DateParser addOneWeek() {
        return new DateParser(date.plusWeeks(1));
    }

    public String asString() {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }

    public static List<LocalDate> getRangeBetween(String start, String end) {
        DateParser startDate = new DateParser(start);
        DateParser endDate = new DateParser(end);

        long days = startDate.date.until(endDate.date, ChronoUnit.DAYS);

        return LongStream.rangeClosed(0, days)
                         .mapToObj(num -> startDate.date.plus(num, ChronoUnit.DAYS).toLocalDate())
                         .collect(Collectors.toList());
    }
}
