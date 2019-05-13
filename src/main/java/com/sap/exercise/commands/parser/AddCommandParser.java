package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.AddCommand;
import com.sap.exercise.commands.Command;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;

public class AddCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(AddCommand.getOptions(), args)) == null)
            return () -> 0;
        Event event;

        if (cmd.getOptions().length > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        if (cmd.hasOption('r')) {
            event = new Event("", Event.EventType.REMINDER);
        } else if (cmd.hasOption('g')) {
            event = new Event("", Event.EventType.GOAL);
        } else {
            event = new Event("", Event.EventType.TASK);
        }

        return new AddCommand(event);
    }
}
