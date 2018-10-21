package com.sap.exercise.commands;

import com.sap.exercise.handler.CRUDOperations;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.List;

public class Agenda implements Command {

    @Override
    public String getName() {
        return "agenda";
    }
    /*this will print the events for a specified time period as :
        <day>  <hour>  <event>
        ...

    (text has configurable color)
     */
    @Override
    public void execute(String... args) {
        try {
            flagHandler(args);

            for (Event event : getEventsInTimeFrame()) {
                printer.printEvent(event);
            }
        } catch (ParseException e) {
            printer.println(e.getMessage());
        }
    }

    private List<Event> getEventsInTimeFrame() {
        return CRUDOperations.getEventsInTimeFrame("2018-10-01 00:00:00", "2018-10-27 23:59:59");
    }

    private void flagHandler(String[] args) throws ParseException {
        CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.timeFrameOptions(), args);

        String startTime = "", endTime = "";
        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e');
        }
    }
}
