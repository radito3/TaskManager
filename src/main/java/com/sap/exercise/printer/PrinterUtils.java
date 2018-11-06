package com.sap.exercise.printer;

import com.sap.exercise.model.Event;

import java.io.PrintStream;
import java.util.Date;
import java.util.stream.Collectors;
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

    static void format(PrintStream writer, Stream<Event> args) {
        final Formatter formatter = new Formatter();
        args.peek(event -> {
                    if (event.getAllDay()) {
                        formatter.setAllDay();
                    }
                    formatter.setType(event.getTypeOf());
                })
                .collect(Collectors.groupingBy(Event::getTimeOf)) //after this the elements aren't sorted by date
                .forEach((cal, eventList) -> {
                    Date date = cal.getTime();
                    writer.print(OutputPrinter.YELLOW + date.toString().substring(0, 10) + OutputPrinter.RESET);

                    eventList.forEach(event -> {
                        if (formatter.isAllDay()) {
                            writer.print("       ");
                        } else {
                            writer.print(" " + date.toString().substring(11, 16) + " ");
                        }
                        formatter.printTitle(writer, event.getTitle());
                        writer.println();
                    });

                    writer.println();
                });
    }

    private static class Formatter {
        private boolean allDay = false;
        private Event.EventType type;

        Formatter() {
        }

        boolean isAllDay() {
            return allDay;
        }

        void setAllDay() {
            this.allDay = true;
        }

        void setType(Event.EventType type) {
            this.type = type;
        }

        void printTitle(PrintStream writer, String title) {
            switch (type) {
                case TASK:
                    writer.print(OutputPrinter.CYAN + title + OutputPrinter.RESET);
                    break;
                case REMINDER:
                    writer.print(OutputPrinter.GREEN + title + OutputPrinter.RESET);
                    break;
                case GOAL:
                    writer.print(OutputPrinter.PURPLE + title + OutputPrinter.RESET);
            }
        }
    }
}
