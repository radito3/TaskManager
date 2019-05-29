package com.sap.exercise.commands;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.EventWrapperFactory;
import com.sap.exercise.model.Event;

public class AddEventCommand implements Command {

    private Event event;
    private final Dao<Event> handler = new EventDao();

    public AddEventCommand(Event event) {
        this.event = event;
    }

    @Override
    public CommandExecutionResult execute() {
        EventWrapper eventWrapper = EventWrapperFactory.getEventWrapper(event);
        EventDataParser dataParser = new EventDataParser(eventWrapper);

        dataParser.parseInput();

        handler.save(eventWrapper.getEvent());
        OutputPrinterProvider.getPrinter().println("\nEvent created");

        return CommandExecutionResult.SUCCESSFUL;
    }
}