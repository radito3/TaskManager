package com.sap.exercise.printer;

import com.sap.exercise.handler.DateHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class PrinterUtils {

    static String getMonth(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            default:
                return "December";
        }
    }

    static void printDay(PrintStream writer, int day, int month, int year, String format) {
        if (isToday(day, month, year))
            writer.printf(OutputPrinter.INVERT + "%2d" + OutputPrinter.RESET + " ", day);
        else
            writer.printf(format + "%2d" + OutputPrinter.RESET + " ", day);
    }

    private static boolean isToday(int day, int month, int year) {
        int[] today = DateHandler.getToday();
        return day == today[0] && month == today[1] && year == today[2];
    }

    static Stream<Map.Entry<Calendar, Set<Event>>> monthEventsSorted(int month, int year, Set<Event> events) {
        return IntStream.rangeClosed(1, DateHandler.getMonthDays()[month])
                .mapToObj(i -> DateHandler.stringifyDate(year, month, i))
                .map(str -> new DateHandler(str).asCalendar())
                .collect(Collectors.toMap(Function.identity(), valueMapper(events)))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().get(Calendar.DAY_OF_MONTH)));
    }

    private static Function<Calendar, Set<Event>> valueMapper(Set<Event> events) {
        return (date) ->
                events.stream()
                        .filter(event -> DateUtils.isSameDay(date, event.getTimeOf()))
                        .collect(Collectors.toSet());
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
