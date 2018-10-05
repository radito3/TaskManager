package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.AbstractEventBuilder;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.sap.exercise.Main.INPUT;

public class Add implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String... args) {
        //if AllDay is true -> Duration will be in number of days
        //if AllDay is false -> Duration is number of minutes
        try {
            Event event = flagHandler(args);

            BufferedReader reader = new BufferedReader(new InputStreamReader(INPUT));

            EventBuilder builder = AbstractEventBuilder.getEventBuilder(event);

            CommandUtils.interactiveInput(reader, printer, builder, event);

            EventsHandler.create(event);
            printer.println("\nEvent created");
        } catch (IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
    }

    //may move this to command utils
    private Event flagHandler(String[] args) throws ParseException {
        CommandLine cmd = CommandUtils.getParsedCmd(args);
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
