package com.sap.exercise.commands;

import java.util.NoSuchElementException;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.builder.EventBuilderFactory;
import com.sap.exercise.model.Event;

//TODO change this command to allow only field specific editing
// rather than having to edit (potentially) every field
public class EditEventCommand implements Command {

    private String eventName;
    private final Dao<Event> eventRepo = new EventDao();

    public EditEventCommand(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public CommandExecutionResult execute() {
        Event event = eventRepo.get(new Property<>("title", eventName))
                               .orElseThrow(() -> new NoSuchElementException("Invalid event name"));
        EventBuilder eventBuilder = EventBuilderFactory.newEventBuilder(event);
        EventDataParser dataParser = new EventDataParser(eventBuilder);

        dataParser.parseInput();
        eventRepo.update(eventBuilder.build());

        OutputPrinterProvider.getPrinter().printf("%nEvent updated");
        return CommandExecutionResult.SUCCESSFUL;
    }
}
