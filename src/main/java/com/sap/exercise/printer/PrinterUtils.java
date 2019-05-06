package com.sap.exercise.printer;

import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

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

    static Map<Calendar, Set<Event>> monthEventsSorted(int month, int year, int numOfMonthDays, Set<Event> events) {
        Map<Calendar, Set<Event>> result = new TreeMap<>(Comparator.comparingInt(cal -> cal.get(Calendar.DAY_OF_MONTH)));
        Set<Event> copiedEvents = new HashSet<>(events);

        for (int i = 1; i <= numOfMonthDays; i++) {
            Calendar key = new GregorianCalendar(year, month - 1, i);
            Set<Event> val =  new HashSet<>();
            for (Event ev : copiedEvents) {
                if (DateUtils.isSameDay(key, ev.getTimeOf())) {
                    val.add(ev);
                }
            }
            result.put(key, val);
        }

        return result;
    }

    static Map<Calendar, List<Event>> mapAndSort(PrintStream writer, Map<Event, Formatter> eventFormatters, Set<Event> events) {
        Map<Calendar, List<Event>> result = new TreeMap<>(Comparator.comparingInt(cal -> cal.get(Calendar.DAY_OF_YEAR)));

        result.putAll(events.stream().collect(Collectors.groupingBy(Event::getTimeOf)));

        result.forEach((cal, list) -> {
            for (int i = 0; i < list.size(); i++) {
                Formatter formatter = new Formatter(writer);
                formatter.setMultipleEvents(list.size() != 1);
                if (i == 0) {
                    formatter.setFirst();
                }
                formatter.setAllDay(list.get(i).getAllDay());
                formatter.setType(list.get(i).getTypeOf());

                eventFormatters.put(list.get(i), formatter);
            }
        });

        return result;
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
