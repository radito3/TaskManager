package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.AddEventCommand;
import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.helper.AddCommandHelper;
import com.sap.exercise.commands.validator.AddCommandValidator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class AddCommandParser extends AbstractCommandParser {

    AddCommandParser() {
        super(new AddCommandHelper(), new AddCommandValidator());
    }

    @Override
    Command parseInternal(CommandLine cmd) {
        Event.EventType eventType;

        if (cmd.hasOption('r')) {
            eventType = Event.EventType.REMINDER;
        } else if (cmd.hasOption('g')) {
            eventType = Event.EventType.GOAL;
        } else {
            eventType = Event.EventType.TASK;
        }

        return new AddEventCommand(eventType);
    }

    @Override
    public Options getOptions() {
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
        Option help = Option.builder()
                .longOpt("help")
                .required(false)
                .desc("Print command help")
                .build();
        return buildOptions(taskOption, reminderOption, goalOption, help);
    }
}
