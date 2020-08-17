package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;

public class EventDeletionListener implements EventListener {

    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];

        SharedResourcesFactory.getAsyncExecutionsService()
                              .execute(() -> deleteEvent(event));
    }

    private void deleteEvent(Event event) {
        SharedResourcesFactory.getEventsCache()
                              .remove(event1 -> event1.getId().equals(event.getId()));
    }
}
