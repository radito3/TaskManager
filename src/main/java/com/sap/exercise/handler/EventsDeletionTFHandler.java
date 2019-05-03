package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

//TODO this inheritor of EventHandler is unneeded
public interface EventsDeletionTFHandler extends EventsHandler<Event> {

    void execute(Event event, String arg1, String arg2);
}
