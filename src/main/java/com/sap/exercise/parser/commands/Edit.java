package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.AbstractEventBuilder;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.sap.exercise.Main.INPUT;

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

            BufferedReader reader = new BufferedReader(new InputStreamReader(INPUT));

            EventBuilder builder = AbstractEventBuilder.getEventBuilder(event);

            CommandUtils.interactiveInput(reader, builder);

            EventsHandler.update(builder.build());
            printer.println("\nEvent updated");
        } catch (NullPointerException npe) {
            printer.println("Invalid event name");
        } catch (IllegalArgumentException iae) {
            printer.println(iae.getMessage());
        }
    }

}
