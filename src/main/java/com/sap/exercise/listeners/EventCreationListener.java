package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.CalendarWrapper;

import java.util.HashSet;
import java.util.Set;

public class EventCreationListener implements EventListener {
    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];
        Integer id = (Integer) args[1];

        event.setId(id);
        CalendarWrapper calendarWrapper = new CalendarWrapper(event.getTimeOf());

        Set<Event> events;
        if ((events = SharedResourcesFactory.getEventsMapService().getFromMap(calendarWrapper)) != null) {
            events.add(event);
        } else {
            events = new HashSet<>();
            events.add(event);
            SharedResourcesFactory.getEventsMapService().putInMap(calendarWrapper, events);
        }
    }
}
