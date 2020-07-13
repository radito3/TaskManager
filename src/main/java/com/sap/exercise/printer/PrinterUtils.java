package com.sap.exercise.printer;

import com.sap.exercise.model.Event;
import com.sap.exercise.util.SimplifiedCalendar;
import org.apache.commons.lang3.time.DateUtils;

import java.io.PrintStream;
import java.util.*;

class PrinterUtils {

    private PrinterUtils() {
    }

    static void printDay(PrintStream printer, int day, int month, int year, String format) {
        Calendar today = Calendar.getInstance();
        Calendar toCheck = new GregorianCalendar(year, month - 1, day);

        if (DateUtils.isSameDay(today, toCheck)) {
            printer.printf(PrinterColors.INVERT + "%2d" + PrinterColors.RESET, day);
        } else {
            printer.printf(format + "%2d" + PrinterColors.RESET, day);
        }
    }

    static Map<SimplifiedCalendar, Set<Event>> getEventsPerDay(Collection<Event> events) {
        Map<SimplifiedCalendar, Set<Event>> result = new TreeMap<>(Comparator.comparingInt(cal -> cal.getDelegate()
                                                                                                     .get(Calendar.DAY_OF_YEAR)));

        for (Event event : events) {
            result.computeIfAbsent(new SimplifiedCalendar(event.getTimeOf()), k -> new TreeSet<>(Comparator.comparing(Event::getTitle)))
                  .add(event);
        }

        return result;
    }

    static Map<Event, Formatter> getEventFormatters(Map<SimplifiedCalendar, Set<Event>> events, PrintStream writer) {
        Map<Event, Formatter> result = new HashMap<>(events.size());

        for (Set<Event> eventsPerDay : events.values()) {
            boolean first = true;

            for (Event event : eventsPerDay) {
                Formatter formatter = new Formatter(writer);
                formatter.setMultipleEvents(eventsPerDay.size() != 1);
                if (first) {
                    first = false;
                    formatter.setFirst();
                }
                formatter.setAllDay(event.getAllDay());
                formatter.setType(event.getTypeOf());

                result.put(event, formatter);
            }
        }

        return result;
    }

}
