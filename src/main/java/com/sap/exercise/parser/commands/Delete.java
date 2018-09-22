package com.sap.exercise.parser.commands;

import com.sap.exercise.handler.EventsHandler;
import com.sap.exercise.model.Event;

public class Delete implements Command {

    @Override
    public String getName() {
        return "delete";
    }

    /*
    <<<FOR FUTURE IMPLEMENTATION>>>

    Optional flags will be for whether to delete a repeatable event in a time frame or every repetition of the event
    If these flags are present for a non-repeatable event, nothing will happen

    delete [start] [end] <event name> //don't know if command args are going to be in that order

    one flag - start time (delete repetitions of event from 'start' argument to end of event
    two flags - delete repetitions of event in specified time frame
     */

    @Override
    public void execute(String... args) {
        try {
            String name = CommandUtils.buildEventName(args);
            Event event = EventsHandler.getObjectFromTitle(name);

            EventsHandler.delete(event);
            printer.println("Event deleted");
        } catch (NullPointerException npe) {
            printer.println("Invalid event name");
        } catch (IllegalArgumentException iae) {
            printer.println(iae.getMessage());
        }
    }

}
