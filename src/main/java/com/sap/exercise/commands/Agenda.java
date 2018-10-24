package com.sap.exercise.commands;

import com.sap.exercise.commands.util.ArgumentEvaluator;
import com.sap.exercise.commands.util.CommandUtils;
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
            String[] times = flagHandler(args);
            String start = times[0], end = times[1].isEmpty() ? "" : times[1] + "-";

            ArgumentEvaluator evaluator = new ArgumentEvaluator(start, end);
            List<Event> events = evaluator.eval(this::getEventsInTimeFrame);

            for (Event event : events) {
                printer.printEvent(event);
            }
        } catch (ParseException e) {
            printer.println(e.getMessage());
        }
    }

    private List<Event> getEventsInTimeFrame(String start, String end) {
        return CRUDOperations.getEventsInTimeFrame(start + " 00:00:00", end + " 23:59:59");
    }

    private String[] flagHandler(String[] args) throws ParseException {
        CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.timeFrameOptions(), args);

        String startTime = "", endTime = "";
        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e');
        }

        return new String[] { startTime, endTime };
    }
}
