package com.sap.exercise.commands;

import com.sap.exercise.Application;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.util.CommandUtils;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;

public class AddCommand implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public int execute(EventHandler handler, String... args) {
        try {
            Event event = flagHandler(args);
            BufferedReader reader = Application.getParser();
            EventWrapper builder = new EventWrapper(event);

            CommandUtils.interactiveInput(reader, builder);

            handler.create(builder.getEvent());
            printer.println("\nEvent created");
        } catch (IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    private Event flagHandler(String[] args) throws ParseException {
        CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.addOptions(), args);

        if (cmd.getOptions().length > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        if (cmd.hasOption('r')) {
            return new Event("", Event.EventType.REMINDER);
        } else if (cmd.hasOption('g')) {
            return new Event("", Event.EventType.GOAL);
        } else {
            return new Event("", Event.EventType.TASK);
        }
    }
}
