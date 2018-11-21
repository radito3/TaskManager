package com.sap.exercise.printer;

import com.sap.exercise.handler.CRUDOperations;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class PrinterUtils {

    static void printDay(PrintStream writer, int day, int month, int year, String format) {
        if (isToday(day, month, year)) {
            writer.printf(OutputPrinter.INVERT + "%1$2d" + OutputPrinter.RESET, day);
        } else {
            writer.printf(format + "%1$2d" + OutputPrinter.RESET, day);
        }
    }

    private static boolean isToday(int day, int month, int year) {
        Calendar today = Calendar.getInstance();
        int year1 = today.get(Calendar.YEAR);
        int month1 = today.get(Calendar.MONTH) + 1;
        int day1 = today.get(Calendar.DAY_OF_MONTH);
        return day == day1 && month == month1 && year == year1;
    }

    static Stream<Map.Entry<Calendar, Set<Event>>> monthEventsSorted(int month, int year, int numOfMonthDays, Set<Event> events) {
        return IntStream.rangeClosed(1, numOfMonthDays)
                .mapToObj(i -> (Calendar) new GregorianCalendar(year, month - 1, i))
                .collect(Collectors.toMap(Function.identity(), valueMapper(events)))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().get(Calendar.DAY_OF_MONTH)));
    }

    private static Function<Calendar, Set<Event>> valueMapper(Set<Event> events) {
        List<CalendarEvents> calEvents = CRUDOperations.getCalendarEventsByEventId(1);
        Stream.Builder<Event> streamBuilder = Stream.builder();

        return (date) -> {
            Stream<Event> result = events.stream()
                    .filter(event -> DateUtils.isSameDay(date, event.getTimeOf()))
                    .peek(event -> {
                        calEvents.forEach(calendarEvents -> {
                            //TODO add new Events with setting their date to the calEvent entry date
                        });
                    });
            return Stream.concat(result, streamBuilder.build())
                    .collect(Collectors.toSet());
        };
    }

    static Stream<Map.Entry<Calendar, List<Event>>> mapAndSort(PrintStream writer, Map<Event, Formatter> eventFormatters, Set<Event> events) {
        return events.stream()
                .peek(event -> {
                    Formatter formatter = new Formatter(writer);
                    formatter.setAllDay(event.getAllDay());
                    formatter.setType(event.getTypeOf());
                    eventFormatters.put(event, formatter);
                })
                .collect(Collectors.groupingBy(Event::getTimeOf))
                .entrySet()
                .stream()
                .map(entry -> {
                    return entry; //TODO apply valueMapper logic here
                })
                .sorted(Comparator.comparingInt(entry -> entry.getKey().get(Calendar.DAY_OF_YEAR)));
    }

    static class Formatter {
        private boolean allDay = false;
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
            if (allDay) {
                writer.print("       ");
            } else {
                writer.print(" " + date.toString().substring(11, 16) + " ");
            }
        }
    }
}
