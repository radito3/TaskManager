package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EventCreationListener implements EventListener {
    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];
        Integer id = (Integer) args[1];

        event.setId(id);
        LocalDate date = event.getTimeOf().toLocalDate();

        Set<Event> events = SharedResourcesFactory.getEventsCache()
                                      .get(date);
        if (events != null) {
            events.add(event);
            return;
        }

        events = new HashSet<>();
        events.add(event);
        SharedResourcesFactory.getEventsCache().put(date, events);
    }
}
