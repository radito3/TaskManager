package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;

public class UpdateListener implements EventListener {
    @Override
    public void notify(Object arg) {
        Event event = (Event) arg;

        SharedResourcesFactory.getEventsMapService()
                .iterateEventsMap((cal, set) -> {
                    if (set.removeIf(event1 -> event1.getId().equals(event.getId()))) {
                        set.add(event);
                    }
                });
    }
}
