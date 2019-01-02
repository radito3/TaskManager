package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

public interface EventsDeletionTFHandler extends EventsHandler<Event> {

    void execute(Event event, String arg1, String arg2);
}
