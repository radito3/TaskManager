package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.AbstractBuilder;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.*;

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

            EventBuilder builder = AbstractBuilder.getEventBuilder(event);

            CommandUtils.interactiveInput(reader, printer, builder, event);

            EventsHandler.create(event);
            printer.println("\nEvent created");
        } catch (IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
    }

    //may move this to command utils
    private Event flagHandler(String[] args) throws ParseException {
        Option task = Option.builder("t")
                .required(false)
                .longOpt("task")
                .desc("Specify the event created to be a Task")
                .build();
        Option reminder = Option.builder("r")
                .required(false)
                .longOpt("reminder")
                .desc("Specify the event created to be a Reminder")
                .build();
        Option goal = Option.builder("g")
                .required(false)
                .longOpt("goal")
                .desc("Specify the event created to be a Goal")
                .build();
        Options options = new Options().addOption(task).addOption(reminder).addOption(goal);

        CommandLine cmd = new DefaultParser().parse(options, args);
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
