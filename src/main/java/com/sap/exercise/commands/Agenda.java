package com.sap.exercise.commands;

import com.sap.exercise.commands.util.ArgumentEvaluator;
import com.sap.exercise.commands.util.CommandUtils;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.ParseException;

import java.util.Set;

public class Agenda implements Command {

    @Override
    public String getName() {
        return "agenda";
    }

    @Override
    public void execute(String... args) {
        try {
            String[] times = CommandUtils.flagHandlerForTimeFrame(args);
            String start = times[0], end = times[1];

            ArgumentEvaluator evaluator = new ArgumentEvaluator(start, end);
            Set<Event> events = evaluator.eval(EventHandler::getEventsInTimeFrame);

            if (events.isEmpty()) {
                printer.println("\nNo upcoming events");
            } else {
                events.forEach(printer::printEvent);  //will change it with printEvents method
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }

}
