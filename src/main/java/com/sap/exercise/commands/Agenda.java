package com.sap.exercise.commands;

import com.sap.exercise.commands.util.ArgumentEvaluator;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.CRUDOperations;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

import java.util.List;

public class Agenda implements Command {

    @Override
    public String getName() {
        return "agenda";
    }

    @Override
    public void execute(String... args) {  //TODO add configurable colour to output text
        try {
            String[] times = CommandUtils.flagHandlerForTimeFrame(args);
            String start = times[0], end = times[1];

            ArgumentEvaluator evaluator = new ArgumentEvaluator(start, end);
            List<Event> events = evaluator.eval(this::getEventsInTimeFrame);

            if (events.isEmpty()) {
                printer.println("\nNo upcoming events"); //may be coloured text
            } else {
                events.forEach(printer::printEvent);  //will change it with printEvents method
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }

    private List<Event> getEventsInTimeFrame(String start, String end) {
        return CRUDOperations.getEventsInTimeFrame(start, end);
    }
}
