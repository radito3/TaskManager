package com.sap.exercise.commands;

import com.sap.exercise.handler.CRUDOperations;
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
            Event event = CRUDOperations.getObjectFromTitle(name);

            CRUDOperations.delete(event);
            printer.println("\nEvent deleted");
        } catch (NullPointerException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }

    /*
    Optional flags will be for whether to delete a repeatable event in a time frame or every repetition of the event
    If these flags are present for a non-repeatable event, nothing will happen

    delete [start] [end] <event name>
     */
    private void flagHandler(String[] args) throws ParseException {
        CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.timeFrameOptions(), args);

        String startTime = "", endTime = "";
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
