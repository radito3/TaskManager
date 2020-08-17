package com.sap.exercise.commands;

import com.sap.exercise.handler.EventDao;
import com.sap.exercise.handler.TimeFrameCondition;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;

import java.util.Collection;

public class PrintAgendaCommand implements Command {

    private final String startTime;
    private final String endTime;
    private final OutputPrinter printer = OutputPrinterProvider.getPrinter();

    public PrintAgendaCommand(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public CommandExecutionResult execute() {
        DateArgumentEvaluator dateArgumentEvaluator = new DateArgumentEvaluator(startTime, endTime);
        Collection<Event> events = dateArgumentEvaluator.eval(this::getEvents);

        if (events.isEmpty()) {
            printer.printf("%nNo upcoming events");
        } else {
            printer.printEvents(events);
        }
        return CommandExecutionResult.SUCCESSFUL;
    }

    private Collection<Event> getEvents(String startTime, String endTime) {
        return new EventDao().getAll(new TimeFrameCondition(startTime, endTime));
    }
}
