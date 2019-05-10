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
        	// This does 'parsing' of args inside an execute method. Shouldn't the execute method be responsible only for executing?
        	// What do you think about introducing CommandParser (read top comment at InputParser)
        	// which would be responsible to parse the input from the user to a Command object (operating on a parsed Event object - composition).
        	// This way you can make a generic CommandParser or even extend the CommandParser and make a parser for each command.
        	// CommandParser could also include the 'InteractiveInput' parsing functionality here.
        	// Potentially, you'd end up with a CommandParser which provides a Command you can directly execute().
        	// Complying with separation of concerns.
        	// Do you think this would improve the current design?
            Event event = flagHandler(args);
            EventWrapper eventWrapper = EventWrapperFactory.getEventWrapper(event);
            InteractiveInput input = new InteractiveInput(reader, eventWrapper);

            input.parseInput();

            new EventCreator().execute(eventWrapper.getEvent());
            printer.println("\nEvent created");
        } catch (IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

	// The name of this method is confusing at first read. flagHandler is a noun,
	// does not contain a verb?
    private Event flagHandler(String[] args) throws ParseException { // The method does several things (parse the input to a
                                                                     // command then it handles different options).
                                                                     // Do you think this could be split up to allow Separation of concerns?
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
        return new Options().addOption(taskOption).addOption(reminderOption).addOption(goalOption);
    }
}