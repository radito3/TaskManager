package com.sap.exercise.commands;

import com.sap.exercise.handler.EventDao;
import com.sap.exercise.handler.GetInTimeFrameOptions;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.ExceptionMessageHandler;

import java.util.Collection;

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
            Collection<Event> events = dateArgumentEvaluator.eval(this::getEvents);

            if (events.isEmpty()) {
                printer.printf("%nNo upcoming events");
            } else {
                printer.printEvents(events);
            }
        } catch (IllegalArgumentException e) {
            ExceptionMessageHandler.setMessage(e.getMessage());
            return CommandExecutionResult.ERROR;
        }
        return CommandExecutionResult.SUCCESSFUL;
    }

    private Collection<Event> getEvents(String startTime, String endTime) {
        return new EventDao().getAll(new GetInTimeFrameOptions(startTime, endTime));
    }
}
