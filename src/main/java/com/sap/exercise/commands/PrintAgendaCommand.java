package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.handler.EventsMapHandler;
import com.sap.exercise.handler.ThreadPoolHandler;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.util.CommandUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

import java.util.Set;

public class PrintAgendaCommand implements Command {

    private ThreadPoolHandler thPool;
    private EventsMapHandler mapHandler;

    public PrintAgendaCommand(ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        this.thPool = thPool;
        this.mapHandler = mapHandler;
    }

    @Override
    public int execute(String... args) {
        try {
            String[] times = CommandUtils.flagHandlerForTimeFrame(args);
            String start = times[0], end = times[1];
            EventsGetterHandler handler = new EventGetter(thPool, mapHandler);

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
