package com.sap.exercise.commands;

import com.sap.exercise.handler.*;
import com.sap.exercise.util.DateArgumentEvaluator;
import com.sap.exercise.util.CommandUtils;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

public class Delete implements Command {

    private Event event;
    private DateArgumentEvaluator evaluator;
    private ThreadPoolHandler thPool;
    private EventsMapHandler mapHandler;

    public Delete(ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        this.thPool = thPool;
        this.mapHandler = mapHandler;
    }

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
            event = new EventGetter(thPool, mapHandler).getEventByTitle(eventName);

            evaluator = new DateArgumentEvaluator(start, end);
            int result = evaluator.eval(this::deleteEvents);

            printer.println(result == 0 ? "\nEvent deleted" : "\nEvent entries deleted");
        } catch (NullPointerException | IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    private int deleteEvents(String start, String end) {
        if (event.getToRepeat() == Event.RepeatableType.NONE || evaluator.numOfArgs() == 0) {
            new EventDeletor(thPool, mapHandler).execute(event);
            return 0;
        }
        new EventDeletorTF(thPool, mapHandler).execute(event, start, end);
        return 1;
    }
}
