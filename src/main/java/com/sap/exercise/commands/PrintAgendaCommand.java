package com.sap.exercise.commands;

import com.sap.exercise.commands.util.DateArgumentEvaluator;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

import java.util.Set;

public class PrintAgendaCommand implements Command {

    @Override
    public String getName() {
        return "agenda";
    }

    @Override
    public int execute(EventHandler handler, String... args) {
        try {
            String[] times = CommandUtils.flagHandlerForTimeFrame(args);
            String start = times[0], end = times[1];

            DateArgumentEvaluator evaluator = new DateArgumentEvaluator(start, end);
            Set<Event> events = evaluator.eval(handler::getEventsInTimeFrame);

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

}
