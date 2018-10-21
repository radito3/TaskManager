package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.AbstractEventBuilder;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;
import com.sap.exercise.parser.InputParser;

import java.io.BufferedReader;

public class Edit implements Command {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public void execute(String... args) {
        try {
            String name = CommandUtils.buildEventName(args);
            Event event = EventsHandler.getObjectFromTitle(name);
            BufferedReader reader = InputParser.getReader();
            EventBuilder builder = AbstractEventBuilder.getEventBuilder(event);

            CommandUtils.interactiveInput(reader, builder);

            EventsHandler.update(builder.build());
            printer.println("\nEvent updated");
        } catch (NullPointerException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }

}
