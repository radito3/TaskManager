package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;

public class DeletionListener implements EventListener {
    @Override
    public void notify(Object arg) {
        Event event = (Event) arg;

        SharedResourcesFactory.getAsyncExecutionsService().execute(() ->
                SharedResourcesFactory.getEventsMapService().iterateEventsMap(
                        (cal, set) -> set.removeIf(event1 -> event1.getId().equals(event.getId()))
                )
        );
    }
}
