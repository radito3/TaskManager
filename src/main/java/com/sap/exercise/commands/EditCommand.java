package com.sap.exercise.commands;

import java.io.BufferedReader;

import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import com.sap.exercise.parser.InputParser;

public class EditCommand implements Command {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public void execute(String... args) {
        try {
            // Dido: I see some dublication in those 'execute' methods of the commands. Any ideas how to reuse those few lines of code?
            String name = CommandUtils.buildEventName(args);
            Event event = EventHandler.getEventByTitle(name);
            BufferedReader reader = InputParser.getReader();
            EventBuilder builder = new EventBuilder(event);

            CommandUtils.interactiveInput(reader, builder);

            EventHandler.update(builder.getEvent());
            printer.println("\nEvent updated");
        } catch (NullPointerException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }

}