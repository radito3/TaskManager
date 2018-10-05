package com.sap.exercise.parser.commands;

import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.*;

public class Delete implements Command {

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public void execute(String... args) {
        try {
            String name = CommandUtils.buildEventName(args);
            Event event = EventsHandler.getObjectFromTitle(name);

            EventsHandler.delete(event);
            printer.println("\nEvent deleted");
        } catch (NullPointerException npe) {
            printer.println("Invalid event name");
        } catch (IllegalArgumentException iae) {
            printer.println(iae.getMessage());
        }
    }

    /*
    Optional flags will be for whether to delete a repeatable event in a time frame or every repetition of the event
    If these flags are present for a non-repeatable event, nothing will happen

    delete [start] [end] <event name>
     */
    private void buildFlagHandler(String[] args) {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to delete entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to delete entries")
                .build();
        Options options = new Options().addOption(start).addOption(end);
        CommandLine cmd;
        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            printer.error(e.getMessage());
            return;
        }
        String startTime = "";
        String endTime = "";
        String eventName = CommandUtils.buildEventName(cmd.getArgs());
        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e');
        }
    }
}
