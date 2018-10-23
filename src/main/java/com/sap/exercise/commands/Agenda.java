package com.sap.exercise.commands;

import com.sap.exercise.handler.CRUDOperations;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

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
            String start = times[0], end = times[1];

            //temporary solution
            List<Event> events;
            if (start.isEmpty() && end.isEmpty()) {
                events = getEventsInTimeFrame();
            } else if (start.isEmpty()) {
                events = getEventsInTimeFrameE(end);
            } else if (end.isEmpty()) {
                events = getEventsInTimeFrameS(start);
            } else {
                events = getEventsInTimeFrame(start, end);
            }

            for (Event event : Objects.requireNonNull(events)) {
                printer.printEvent(event);
            }
        } catch (ParseException e) {
            printer.println(e.getMessage());
        }
    }

    //very inefficient implementation
    private List<Event> getEventsInTimeFrame() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = cal.get(Calendar.DAY_OF_MONTH);
        return getEventsInTimeFrame(String.valueOf(year + "-" + month + "-" + day),
                String.valueOf(year + "-" + month + "-" + (day + 7)));
    }

    private List<Event> getEventsInTimeFrameS(String val) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = cal.get(Calendar.DAY_OF_MONTH);
        return getEventsInTimeFrame(val, String.valueOf(year + "-" + month + "-" + (day + 7)));
    }

    private List<Event> getEventsInTimeFrameE(String val) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = cal.get(Calendar.DAY_OF_MONTH);
        return getEventsInTimeFrame(String.valueOf(year + "-" + month + "-" + day), val);
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
