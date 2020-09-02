package com.sap.exercise.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DateParser {

    //TODO these formats aren't necessarily compatible with DateTimeFormatter
    public static final String[] DATE_FORMATS = new String[] { "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy", "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd MMM yyyy HH:mm", "dd MMM yyyy", "yyyy-MM-dd" };

    private final LocalDateTime date;

    public DateParser(String text) {
        TemporalAccessor date = tryParse(text.trim());
        if (date == null) {
            throw new IllegalArgumentException("Unsupported date format");
        }
        this.date = LocalDateTime.from(date);
    }

    private TemporalAccessor tryParse(String text) {
        for (String format : DATE_FORMATS) {
            try {
                return DateTimeFormatter.ofPattern(format).parse(text);
            } catch (DateTimeParseException ex) {
                //ignored
            }
        }
        return null;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String asString() {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }

    public static List<LocalDate> getRangeBetween(String start, String end) {
        DateParser startDate = new DateParser(start);
        DateParser endDate = new DateParser(end);

        long days = startDate.date.until(endDate.date, ChronoUnit.DAYS);

        return LongStream.rangeClosed(0, days)
                         .mapToObj(num -> startDate.date.plusDays(num).toLocalDate())
                         .collect(Collectors.toList());
    }
}
