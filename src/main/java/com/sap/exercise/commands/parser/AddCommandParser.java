package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.AddEventCommand;
import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.helper.CommandHelper;
import com.sap.exercise.commands.validator.AddCommandValidator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.function.Function;

public class AddCommandParser extends AbstractCommandParser {

    AddCommandParser(Function<Options, CommandHelper> helperCreator) {
        super(helperCreator, AddCommandValidator::new);
    }

    @Override
    public Command parse(String[] args) throws ParseException {
        Command result = super.parse(args);
        if (result != null)
            return result;

        Event event;

        if (cmd.hasOption('r')) {
            event = new Event("", Event.EventType.REMINDER);
        } else if (cmd.hasOption('g')) {
            event = new Event("", Event.EventType.GOAL);
        } else {
            event = new Event("", Event.EventType.TASK);
        }

        return new AddEventCommand(event);
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
