package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.CalendarWrapper;
import com.sap.exercise.util.DateHandler;

public class DeletionInTimeFrameListener implements EventListener {
    @Override
    public void notify(Object arg) {
        Object[] objects = (Object[]) arg;
        Event event = (Event) objects[0];
        String[] timeFrame = (String[]) objects[1];
        DateHandler dateHandler = new DateHandler(timeFrame[0], timeFrame[1]);

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
