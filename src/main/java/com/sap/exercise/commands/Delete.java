package com.sap.exercise.commands;

import com.sap.exercise.commands.util.ArgumentEvaluator;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

public class Delete implements Command {

    private Event event;
    private ArgumentEvaluator evaluator;
    
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
            event = EventHandler.getEventByTitle(eventName);

            evaluator = new ArgumentEvaluator(start, end);
            int result = evaluator.eval(this::deleteEvents);

            printer.println(result == 0 ? "\nEvent deleted" : "\nEvent entries deleted");
        } catch (NullPointerException | IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
    }

    private int deleteEvents(String start, String end) {
        if (event.getToRepeat() == Event.RepeatableType.NONE || evaluator.numOfArgs() == 0) {
            EventHandler.delete(event);
            return 0;
        }
        EventHandler.deleteInTimeFrame(event, start, end);
        return 1;
    }
}
