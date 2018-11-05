package com.sap.exercise.printer;

import com.sap.exercise.model.Event;

import java.io.PrintStream;
import java.util.Date;
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
            .forEach(event -> {
                Date date = event.getTimeOf().getTime();
                writer.print(date.toString().substring(0, 10));

                if (formatter.isAllDay()) {
                    writer.print("       ");
                } else {
                    writer.print(" " + date.toString().substring(11, 16) + " ");
                }

                switch (formatter.getType()) {
                    case TASK:
                        writer.print(OutputPrinter.CYAN + event.getTitle() + OutputPrinter.RESET);
                        break;
                    case REMINDER:
                        writer.print(OutputPrinter.GREEN + event.getTitle() + OutputPrinter.RESET);
                        break;
                    case GOAL:
                        writer.print(OutputPrinter.PURPLE + event.getTitle() + OutputPrinter.RESET);
                }

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

        Event.EventType getType() {
            return type;
        }

        void setType(Event.EventType type) {
            this.type = type;
        }
    }
}
