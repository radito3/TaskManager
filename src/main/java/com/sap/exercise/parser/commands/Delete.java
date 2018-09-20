package com.sap.exercise.parser.commands;

import com.sap.exercise.db.DatabaseUtil;
import com.sap.exercise.db.DatabaseUtilFactory;
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
            String name = buildEventName(args);
            DatabaseUtil db = DatabaseUtilFactory.getDbClient();

            Event event = db.getObject(s ->
                    s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'" + name + "\';", Event.class)
                            .uniqueResultOptional().orElseThrow(NullPointerException::new));

            db.processObject(s -> s.delete(event));
            printer.print("Event deleted");
        } catch (NullPointerException npe) {
            printer.print("Invalid event name");
        } catch (IllegalArgumentException iae) {
            printer.print("Event name not specified");
        }
    }

    private String buildEventName(String[] input) {
        if (input.length == 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(input[1]);
        for (int i = 2; i < input.length; i++) sb.append(' ').append(input[i]);
        return sb.toString();
    }
}
