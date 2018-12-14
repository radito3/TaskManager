package com.sap.exercise.commands;

import java.io.BufferedReader;

import com.sap.exercise.wrapper.EventWrapper;
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
    public void execute(EventHandler handler, String... args) {
        try {
            BufferedReader reader = new InputParser().getReader();

            String name = CommandUtils.buildEventName(args);
            Event event = handler.getEventByTitle(name);
            EventWrapper builder = new EventWrapper(event);

            CommandUtils.interactiveInput(reader, builder);

            handler.update(builder.getEvent());
            printer.println("\nEvent updated");
        } catch (NullPointerException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }

}
