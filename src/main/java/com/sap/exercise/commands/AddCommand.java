package com.sap.exercise.commands;

import com.sap.exercise.handler.EventCreator;
import com.sap.exercise.handler.EventsMapHandler;
import com.sap.exercise.handler.ThreadPoolHandler;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.util.CommandUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;

public class AddCommand implements Command {

    private BufferedReader reader;
    private ThreadPoolHandler thPool;
    private EventsMapHandler mapHandler;

    public AddCommand(BufferedReader reader, ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        this.reader = reader;
        this.thPool = thPool;
        this.mapHandler = mapHandler;
    }

    @Override
    public int execute(String... args) {
        try {
            Event event = flagHandler(args);
            EventWrapper builder = new EventWrapper(event);

            CommandUtils.interactiveInput(reader, builder);

            new EventCreator(thPool, mapHandler).execute(builder.getEvent());
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
