package com.sap.exercise.printer;

import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class PrinterUtils {

    private PrinterUtils() {
    }

    static void printDay(PrintStream writer, int day, int month, int year, String format) {
        Calendar today = Calendar.getInstance();
        Calendar toCheck = new GregorianCalendar(year, month - 1, day);

        if (DateUtils.isSameDay(today, toCheck)) {
            writer.printf(OutputPrinter.INVERT + "%1$2d" + OutputPrinter.RESET, day);
        } else {
            writer.printf(format + "%1$2d" + OutputPrinter.RESET, day);
        }
    }

    static Set<Map.Entry<Calendar, Set<Event>>> monthEventsSorted(int month, int year, int numOfMonthDays, Set<Event> events) {
        Set<Map.Entry<Calendar, Set<Event>>> result =
                new TreeSet<>(Comparator.comparingInt(entry -> entry.getKey().get(Calendar.DAY_OF_MONTH)));
        result.addAll(
                IntStream.rangeClosed(1, numOfMonthDays)
                        .mapToObj(i -> (Calendar) new GregorianCalendar(year, month - 1, i))
                        .collect(Collectors.toMap(
                                Function.identity(),
                                (Calendar date) -> {
                                    Set<Event> filteredEvents = new HashSet<>(events);
                                    filteredEvents.removeIf(event -> !DateUtils.isSameDay(date, event.getTimeOf()));
                                    return filteredEvents;
                                }))
                        .entrySet()
        );
        return Collections.unmodifiableSet(result);
    }

    static Stream<Map.Entry<Calendar, List<Event>>> mapAndSort(PrintStream writer, Map<Event, Formatter> eventFormatters, Set<Event> events) {
        return events.stream()
                .collect(Collectors.groupingBy(Event::getTimeOf))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().get(Calendar.DAY_OF_YEAR)))
                .peek(entry -> {
                    List<Event> eventList = entry.getValue();

                    for (int i = 0; i < eventList.size(); i++) {
                        Formatter formatter = new Formatter(writer);
                        formatter.setMultipleEvents(eventList.size() != 1);
                        if (i == 0) {
                            formatter.setFirst();
                        }
                        formatter.setAllDay(eventList.get(i).getAllDay());
                        formatter.setType(eventList.get(i).getTypeOf());
                        eventFormatters.put(eventList.get(i), formatter);
                    }
                });
    }

    //TODO needs complete overhaul
    static class Formatter {
        private boolean allDay = false;
        private boolean multipleEvents = false;
        private boolean first = false;
        private Event.EventType type;
        private PrintStream writer;

        Formatter(PrintStream writer) {
            this.writer = writer;
        }

        void setAllDay(boolean allDay) {
            this.allDay = allDay;
        }

        void setType(Event.EventType type) {
            this.type = type;
        }

        void setMultipleEvents(boolean val) {
            this.multipleEvents = val;
        }

        void setFirst() {
            this.first = true;
        }

        void printTitle(String title) {
            switch (type) {
                case TASK:
                    writer.print(OutputPrinter.CYAN + title + OutputPrinter.RESET);
                    break;
                case REMINDER:
                    writer.print(OutputPrinter.GREEN + title + OutputPrinter.RESET);
                    break;
                case GOAL:
                    writer.print(OutputPrinter.PURPLE + title + OutputPrinter.RESET);
                    break;
            }
        }

        void printTime(Date date) {
            if (multipleEvents && !first) {
                writer.print("          ");
            }
            if (allDay) {
                writer.print("       ");
            } else {
                writer.print(" " + date.toString().substring(11, 16) + " ");
            }
        }
    }
}
