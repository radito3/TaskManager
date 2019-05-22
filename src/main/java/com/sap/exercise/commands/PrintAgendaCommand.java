package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.ExceptionMessageHandler;

import java.util.Set;

public class PrintAgendaCommand implements Command {

    private String startTime, endTime;

    public PrintAgendaCommand(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public CommandExecutionResult execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try {
            DateArgumentEvaluator dateArgumentEvaluator = new DateArgumentEvaluator(startTime, endTime);
            Set<Event> events = dateArgumentEvaluator.eval(new EventGetter()::getEventsInTimeFrame);

            if (events.isEmpty()) {
                printer.println("\nNo upcoming events");
            } else {
                printer.printEvents(events);
            }
        } catch (IllegalArgumentException e) {
            ExceptionMessageHandler.setMessage(e.getMessage());
            return CommandExecutionResult.ERROR;
        }
        return CommandExecutionResult.SUCCESSFUL;
    }
}
