package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.SimplifiedCalendar;
import com.sap.exercise.util.DateHandler;

public class EventDeletionInTimeFrameListener implements EventListener {
    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];
        DateHandler dateHandler = new DateHandler((String) args[1], (String) args[2]);

        SharedResourcesFactory.getAsyncExecutionsService().execute(() -> {
            for (SimplifiedCalendar calWrapper : dateHandler.getTimeRange()) {
                SharedResourcesFactory.getEventsMapService().iterateEventsMap((date, eventSet) -> {
                    if (calWrapper.equals(date)) {
                        eventSet.removeIf(event1 -> event1.getId().equals(event.getId()));
                    }
                });
            }
        });
    }
}
