package com.sap.exercise.commands;

import java.util.NoSuchElementException;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.EventWrapperFactory;
import com.sap.exercise.model.Event;

public class EditEventCommand implements Command {

    private String eventName;
    private final Dao<Event> handler = new EventDao();

    public EditEventCommand(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public CommandExecutionResult execute() {
        Event event = handler.get(new Property<>("title", eventName))
                .orElseThrow(() -> new NoSuchElementException("Invalid event name"));
        EventWrapper eventWrapper = EventWrapperFactory.getEventWrapper(event);
        EventDataParser dataParser = new EventDataParser(eventWrapper);

        dataParser.parseInput();

        handler.update(eventWrapper.getEvent());
        OutputPrinterProvider.getPrinter().printf("%nEvent updated");
        return CommandExecutionResult.SUCCESSFUL;
    }
}
