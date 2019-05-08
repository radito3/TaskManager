package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

import java.util.Set;

public class PrintAgendaCommand implements Command {

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

}
