package com.sap.exercise.listeners;

import com.sap.exercise.handler.ListenableEventType;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;

public class DeletionListener implements EventListener {
    @Override
    public void notify(Object... args) {
        ListenableEventType type = (ListenableEventType) args[0];
        if (type == ListenableEventType.DELETE) {
            Event event = (Event) args[1];

            SharedResourcesFactory.getAsyncExecutionsService().execute(() ->
                    SharedResourcesFactory.getEventsMapService().iterateEventsMap(
                            (cal, set) -> set.removeIf(event1 -> event1.getId().equals(event.getId()))
                    )
            );
        }
    }
}
