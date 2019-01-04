package com.sap.exercise.commands;

import java.io.BufferedReader;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventUpdater;
import com.sap.exercise.handler.EventsMapHandler;
import com.sap.exercise.handler.ThreadPoolHandler;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.util.CommandUtils;
import com.sap.exercise.model.Event;

public class EditCommand implements Command {

    private BufferedReader reader;
    private ThreadPoolHandler thPool;
    private EventsMapHandler mapHandler;

    public EditCommand(BufferedReader reader, ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        this.reader = reader;
        this.thPool = thPool;
        this.mapHandler = mapHandler;
    }

    @Override
    public int execute(String... args) {
        try {
            String name = CommandUtils.buildEventName(args);
            Event event = new EventGetter(thPool, mapHandler).getEventByTitle(name);
            EventWrapper builder = new EventWrapper(event);

            CommandUtils.interactiveInput(reader, builder);

            new EventUpdater(thPool, mapHandler).execute(builder.getEvent());
            printer.println("\nEvent updated");
        } catch (NullPointerException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

}
