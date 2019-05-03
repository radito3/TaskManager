package com.sap.exercise.commands;

import com.sap.exercise.handler.EventDeletor;
import com.sap.exercise.handler.EventDeletorTF;
import com.sap.exercise.handler.EventGetter;
//import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

import java.util.NoSuchElementException;

public class Delete implements Command {

    private Event event;
    private DateArgumentEvaluator evaluator;
//    private EventsHandler<?> handler;//for after refactoring of handlers

    @Override
    public int execute(String... args) {
        try {
            String[] vars = CommandUtils.flagHandlerForTimeFrame(
                    args,
                    cmd -> CommandUtils.buildEventName(cmd.getArgs())
            );
            String start = vars[0],
                    end = vars[1],
                    eventName = vars[2];
            event = new EventGetter().getEventByTitle(eventName);

            evaluator = new DateArgumentEvaluator(start, end);
            int result = evaluator.eval(this::deleteEvents);

            printer.println(result == 0 ? "\nEvent deleted" : "\nEvent entries deleted");
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    private int deleteEvents(String start, String end) {
        if (event.getToRepeat() == Event.RepeatableType.NONE || evaluator.numOfArgs() == 0) {
            new EventDeletor().execute(event);
            return 0;
        }
        new EventDeletorTF().execute(event, start, end);
        return 1;
    }
}
