package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.AddEventCommand;
import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class AddCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(addCommandOptions(), args)) == null)
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

        return new AddEventCommand(event);
    }

    public static Options addCommandOptions() {
        Option taskOption = Option.builder("t")
                .required(false)
                .longOpt("task")
                .desc("Create a Task (default)")
                .build();
        Option reminderOption = Option.builder("r")
                .required(false)
                .longOpt("reminder")
                .desc("Create a Reminder")
                .build();
        Option goalOption = Option.builder("g")
                .required(false)
                .longOpt("goal")
                .desc("Create a Goal")
                .build();
        return CommandUtils.buildOptions(taskOption, reminderOption, goalOption);
    }
}
