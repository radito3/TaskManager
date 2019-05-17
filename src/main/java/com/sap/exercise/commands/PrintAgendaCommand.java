package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;

import java.util.Set;

public class PrintAgendaCommand implements Command {

    private String start, end;

    public PrintAgendaCommand(String start, String end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try {
            DateArgumentEvaluator dateArgumentEvaluator = new DateArgumentEvaluator(start, end);
            Set<Event> events = dateArgumentEvaluator.eval(new EventGetter()::getEventsInTimeFrame);

            if (events.isEmpty()) {
                printer.println("\nNo upcoming events");
            } else {
                OutputPrinterProvider.getPrinter().printEvents(events);
            }
        } catch (IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }
}
