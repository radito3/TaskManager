package com.sap.exercise.commands;

import com.sap.exercise.handler.EventCreator;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.EventWrapperFactory;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;

public class AddCommand implements Command, CommandOptions {

    private BufferedReader reader;

    public AddCommand(BufferedReader reader) {
        this.reader = reader;
    }

    AddCommand() {
    }

    @Override
    public int execute(String... args) {
        try {
            Event event = flagHandler(args);
            EventWrapper wrapper = EventWrapperFactory.getEventWrapper(event);
            InteractiveInput input = new InteractiveInput(reader, wrapper);

            input.parseInput();

            new EventCreator().execute(wrapper.getEvent());
            printer.println("\nEvent created");
        } catch (IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    private Event flagHandler(String[] args) throws ParseException {
        CommandLine cmd = CommandUtils.getParsedCmd(getOptions(), args);

        if (cmd.getOptions().length > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        if (cmd.hasOption('r')) {
            return new Event("", Event.EventType.REMINDER);
        } 
        if (cmd.hasOption('g')) {
            return new Event("", Event.EventType.GOAL);
        } 
        return new Event("", Event.EventType.TASK);
    }

    @Override
    public Options getOptions() {
        Option task = Option.builder("t")
                .required(false)
                .longOpt("task")
                .desc("Create a Task (default)")
                .build();
        Option reminder = Option.builder("r")
                .required(false)
                .longOpt("reminder")
                .desc("Create a Reminder")
                .build();
        Option goal = Option.builder("g")
                .required(false)
                .longOpt("goal")
                .desc("Create a Goal")
                .build();
        return new Options().addOption(task).addOption(reminder).addOption(goal);
    }
}
