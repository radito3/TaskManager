package com.sap.exercise.parser.commands;

import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

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
    private void flagHandler(String[] args) {
        CommandLine cmd;
        try {
            cmd = CommandUtils.getParsedCmd(CommandUtils.deleteOptions(), args);
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
        //deleteInTimeFrame(startTime, endTime);
    }
}
