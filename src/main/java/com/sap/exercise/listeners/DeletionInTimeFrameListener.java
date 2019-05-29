package com.sap.exercise.listeners;

import com.sap.exercise.handler.ListenableEventType;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.CalendarWrapper;
import com.sap.exercise.util.DateHandler;

public class DeletionInTimeFrameListener implements EventListener {
    @Override
    public void notify(Object... args) {
        ListenableEventType type = (ListenableEventType) args[0];
        if (type == ListenableEventType.DELETE_IN_TIME_FRAME) {
            Event event = (Event) args[1];
            DateHandler dateHandler = new DateHandler((String) args[2], (String) args[3]);

            SharedResourcesFactory.getAsyncExecutionsService().execute(() -> {
                for (CalendarWrapper calWrapper : dateHandler.fromTo()) {
                    SharedResourcesFactory.getEventsMapService().iterateEventsMap((date, eventSet) -> {
                        if (calWrapper.equals(date)) {
                            eventSet.removeIf(event1 -> event1.getId().equals(event.getId()));
                        }
                    });
                }
            });
        }
    }
}
