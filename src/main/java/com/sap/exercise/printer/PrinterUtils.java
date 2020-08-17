package com.sap.exercise.printer;

import com.sap.exercise.model.Event;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class PrinterUtils {

    private PrinterUtils() {
    }

    static void printDay(PrintStream printer, LocalDate date, String format) {
        LocalDate today = LocalDate.now();

        if (today.equals(date)) {
            printer.printf(PrinterColors.INVERT + "%2d" + PrinterColors.RESET, date.getDayOfMonth());
        } else {
            printer.printf(format + "%2d" + PrinterColors.RESET, date.getDayOfMonth());
        }
    }

    static Map<LocalDate, Set<Event>> getEventsPerDay(Collection<Event> events) {
        Map<LocalDate, Set<Event>> result = new TreeMap<>();

        for (Event event : events) {
            result.computeIfAbsent(event.getTimeOf().toLocalDate(), k -> new TreeSet<>(Comparator.comparing(Event::getTimeOf)))
                  .add(event);
        }

        return result;
    }

    static Map<Event, Formatter> getEventFormatters(Map<LocalDate, Set<Event>> events) {
        Map<Event, Formatter> result = new HashMap<>(events.size());

        for (Set<Event> eventsPerDay : events.values()) {
            boolean first = true;

            for (Event event : eventsPerDay) {
                Formatter formatter = new Formatter(event);
                formatter.setMultipleEvents(eventsPerDay.size() != 1);
                if (first) {
                    first = false;
                    formatter.setFirst();
                }

                result.put(event, formatter);
            }
        }

        return result;
    }

}
