package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Set;

public class PrintAgendaCommand extends AbstractCommand implements Command {

    @Override
    public int execute(String... args) {
        try {
            // Take a look at first comment inside execute method of AddCommand class
            String[] times = CommandUtils.flagHandlerForTimeFrame(args);
            String startDate = times[0], endDate = times[1];
            EventsGetterHandler eventsGetterHandler = new EventGetter(); // Do not be afraid to make descriptive variable names

            DateArgumentEvaluator dateArgumentEvaluator = new DateArgumentEvaluator(startDate, endDate); // Do not be afraid to make descriptive variable names
            Set<Event> events = dateArgumentEvaluator.eval(eventsGetterHandler::getEventsInTimeFrame);

            if (events.isEmpty()) {
                printer.println("\nNo upcoming events");
            } else {
                printer.printEvents(events);
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    static Options getOptions() {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to get entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to get entries")
                .build();
        return CommandUtils.buildOptions(start, end);
    }
}
