package com.sap.exercise.parser.commands;

import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;

public class Add implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String... args) {
        //if AllDay is true -> Duration will be in number of days
        //if AllDay is false -> Duration is number of minutes

        /* temporary implementation */
        String name = buildEventName(args);
        Event event = new Event(name);
        EventsHandler.create(event);

        printer.println("Event created");
    }

    /*
    when starting this command the optional flags will be:
        -t[--task] for a task;
        -r[--reminder] for a reminder;
        -g[--goal] for a goal.
    default event created is task

    after typing the command the respective fields of the event created will appear and input for them will prompt user

    method will be the same as in edit with the difference that no prior data will be in the Event object
     */

    private String flagHandler(String input) {
        return input.startsWith("--") ? input.replace(input.charAt(1), input.charAt(2)).substring(0, 2) : input;
    }

    //temporary
    private String buildEventName(String[] input) {
        StringBuilder sb = new StringBuilder(input[1]);
        for (int i = 2; i < input.length; i++) sb.append(' ').append(input[i]);
        return sb.toString();
    }
}
