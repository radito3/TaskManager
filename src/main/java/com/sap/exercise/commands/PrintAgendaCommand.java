package com.sap.exercise.commands;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.handler.TimeFrameCondition;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.model.Event;

import java.util.Collection;

public class PrintAgendaCommand implements Command {

    private final String startTime;
    private final String endTime;
    private final OutputPrinter printer = OutputPrinterProvider.getPrinter();
    private final Dao<Event> eventRepo = new EventDao();

    public PrintAgendaCommand(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public CommandExecutionResult execute() {
        Collection<Event> events = eventRepo.getAll(new TimeFrameCondition(startTime, endTime));

        if (events.isEmpty()) {
            printer.printf("%nNo upcoming events");
        } else {
            printer.printEvents(events);
        }
        return CommandExecutionResult.SUCCESSFUL;
    }
}
