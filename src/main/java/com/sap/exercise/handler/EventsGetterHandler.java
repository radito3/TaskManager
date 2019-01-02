package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

import java.util.Set;

public interface EventsGetterHandler extends EventsHandler<Event> {

    Event getEventByTitle(String var);

    Set<Event> getEventsInTimeFrame(String var1, String var2);
}
