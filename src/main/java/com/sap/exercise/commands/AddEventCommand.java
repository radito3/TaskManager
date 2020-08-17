package com.sap.exercise.commands;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.builder.EventBuilderFactory;
import com.sap.exercise.model.Event;

//TODO for this and the Edit command, when migrating to a rest service
// it'd probably be best if they are web socket endpoints,
// so as not to send a http request on every event field
public class AddEventCommand implements Command {

    private final Event.EventType eventType;

    public AddEventCommand(Event.EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public CommandExecutionResult execute() {
        EventBuilder eventBuilder = EventBuilderFactory.newEventBuilder(eventType);
        EventDataParser dataParser = new EventDataParser(eventBuilder);
        Dao<Event> eventRepo = new EventDao();

        dataParser.parseInput();
        eventRepo.save(eventBuilder.build());

        OutputPrinterProvider.getPrinter().printf("%nEvent created");
        return CommandExecutionResult.SUCCESSFUL;
    }
}