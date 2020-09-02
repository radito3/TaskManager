package com.sap.exercise.commands;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.DeleteCondition;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.model.Event;

import java.util.NoSuchElementException;

public class DeleteEventCommand implements Command {

    private final String start;
    private final String end;
    private final String eventName;
    private final boolean toExecute;

    private final Dao<Event> handler = new EventDao();

    public DeleteEventCommand(String start, String end, String eventName, boolean toExecute) {
        this.start = start;
        this.end = end;
        this.eventName = eventName;
        this.toExecute = toExecute;
    }

    @Override
    public CommandExecutionResult execute() {
        Event event = handler.get(new Property<>("title", eventName))
                       .orElseThrow(() -> new NoSuchElementException("Invalid event name"));

        handler.delete(event, new DeleteCondition(start, end, toExecute));

        OutputPrinterProvider.getPrinter().printf("%nEvent entries deleted");
        return CommandExecutionResult.SUCCESSFUL;
    }
}
