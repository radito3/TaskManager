package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;

public class EventUpdateListener implements EventListener {

    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];

        SharedResourcesFactory.getEventsCache()
                              .forAllEvents(events -> {
            if (events.removeIf(event1 -> event1.getId().equals(event.getId()))) {
                events.add(event);
            }
        });
    }
}
