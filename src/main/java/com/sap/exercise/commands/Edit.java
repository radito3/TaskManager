package com.sap.exercise.commands;

import com.sap.exercise.builder.AbstractEventBuilder;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.CRUDOperations;
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
            Event event = CRUDOperations.getObjectFromTitle(name);
            BufferedReader reader = InputParser.getReader();
            EventBuilder builder = AbstractEventBuilder.getEventBuilder(event);

            CommandUtils.interactiveInput(reader, builder);

            CRUDOperations.update(builder.build());
            printer.println("\nEvent updated");
        } catch (NullPointerException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }

}
