package com.sap.exercise.printer;

import com.sap.exercise.model.Event;

import java.io.PrintStream;
import java.util.Calendar;

class Formatter {

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
                writer.print(PrinterColors.CYAN + title + PrinterColors.RESET);
                break;
            case REMINDER:
                writer.print(PrinterColors.GREEN + title + PrinterColors.RESET);
                break;
            case GOAL:
                writer.print(PrinterColors.PURPLE + title + PrinterColors.RESET);
                break;
        }
    }

    void printTime(Calendar date) {
        if (multipleEvents && !first) {
            writer.print("          ");
        }
        if (allDay) {
            writer.print("       ");
        } else {
            writer.printf(" %2tk:%1$2tM:%1$2tS ", date);
        }
    }
}
