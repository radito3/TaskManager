package com.sap.exercise.commands;

import com.sap.exercise.commands.util.DateArgumentEvaluator;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

public class Delete implements Command {

    private Event event;
    private DateArgumentEvaluator evaluator;
    private EventHandler handler = EventHandler.getInstance();
    
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public void execute(String... args) {
        try {
            String[] vars = CommandUtils.flagHandlerForTimeFrame(
                    args,
                    cmd -> CommandUtils.buildEventName(cmd.getArgs())
            );
            String start = vars[0],
                    end = vars[1],
                    eventName = vars[2];
            event = handler.getEventByTitle(eventName);

            evaluator = new DateArgumentEvaluator(start, end);
            int result = evaluator.eval(this::deleteEvents);

            printer.println(result == 0 ? "\nEvent deleted" : "\nEvent entries deleted");
        } catch (NullPointerException | IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
    }

    private int deleteEvents(String start, String end) {
        if (event.getToRepeat() == Event.RepeatableType.NONE || evaluator.numOfArgs() == 0) {
            handler.delete(event);
            return 0;
        }
        handler.deleteInTimeFrame(event, start, end);
        return 1;
    }
}
