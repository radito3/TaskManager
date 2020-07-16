package com.sap.exercise.printer;

import com.sap.exercise.model.Event;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

class Formatter {

    private Event event;
    private boolean multipleEvents;
    private boolean first = false;

    Formatter(Event event) {
        this.event = event;
    }

    void setMultipleEvents(boolean val) {
        this.multipleEvents = val;
    }

    void setFirst() {
        this.first = true;
    }

    void printTitle(PrintStream writer) {
        switch (event.getTypeOf()) {
            case TASK:
                writer.print(PrinterColors.CYAN + event.getTitle() + PrinterColors.RESET);
                break;
            case REMINDER:
                writer.print(PrinterColors.GREEN + event.getTitle() + PrinterColors.RESET);
                break;
            case GOAL:
                writer.print(PrinterColors.PURPLE + event.getTitle() + PrinterColors.RESET);
                break;
        }
    }

    void printTime(PrintStream writer) {
        if (multipleEvents && !first) {
            writer.print("          ");
        }
        if (event.getAllDay()) {
            writer.print("       ");
        } else {
            String date = DateTimeFormatter.ofPattern(" k:mm:ss ").format(event.getTimeOf());
            writer.print(date);
        }
    }
}
