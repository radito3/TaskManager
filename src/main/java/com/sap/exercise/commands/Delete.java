package com.sap.exercise.commands;

import com.sap.exercise.commands.util.ArgumentEvaluator;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.CRUDOperations;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

public class Delete implements Command {

    private Event event;
    
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public void execute(String... args) {
        try {
            String[] vars = CommandUtils.flagHandlerForTimeFrame(args, cmd -> CommandUtils.buildEventName(cmd.getArgs()));
            String start = vars[0], end = vars[1], eventName = vars[2];
            event = CRUDOperations.getObjectFromTitle(eventName);
            

            ArgumentEvaluator evaluator = new ArgumentEvaluator(start, end);
            evaluator.eval(this::deleteInTimeFrame);

            CRUDOperations.delete(event);
            printer.println("\nEvent deleted");
        } catch (NullPointerException | IllegalArgumentException | ParseException e) {
            printer.println(e.getMessage());
        }
    }

    private int deleteInTimeFrame(String start, String end) {
        //perform check if event is repeatable
        //if not -> delete event
        //if yes -> delete in time frame
        CRUDOperations.deleteEventsInTimeFrame(event, start, end);
        return 0;
    }
}
