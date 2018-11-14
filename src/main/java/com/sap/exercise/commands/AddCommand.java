package com.sap.exercise.commands;

import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import com.sap.exercise.parser.InputParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;

public class AddCommand implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String... args) {
        try {
            Event event = flagHandler(args);
            BufferedReader reader = InputParser.getReader();
            EventBuilder builder = new EventBuilder(event);

            CommandUtils.interactiveInput(reader, builder);

            EventHandler.create(builder.getEvent());
            printer.println("\nEvent created");
        } catch (IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
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
