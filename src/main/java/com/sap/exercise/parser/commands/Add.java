package com.sap.exercise.parser.commands;

import com.sap.exercise.builder.AbstractBuilder;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.sap.exercise.Main.INPUT;

public class Add implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String... args) {
        //if AllDay is true -> Duration will be in number of days
        //if AllDay is false -> Duration is number of minutes

        Event event = new Event("-", args.length == 1 ? Event.EventType.TASK :
                Event.EventType.valueOf(flagHandler(args[1])));

        BufferedReader reader = new BufferedReader(new InputStreamReader(INPUT));

        EventBuilder builder = AbstractBuilder.getEventBuilder(event);

        CommandUtils.interactiveInput(reader, printer, builder, event);

        EventsHandler.create(event);
        printer.println("Event created");
    }

    /*
    when starting this command the optional flags will be:
        -t[--task] for a task;
        -r[--reminder] for a reminder;
        -g[--goal] for a goal.
    default event created is task
     */

    private String flagHandler(String input) {
        return input.startsWith("--") ? input.replace(input.charAt(1), input.charAt(2)).substring(0, 2) : input;
    }

}
