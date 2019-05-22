package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.util.CalendarWrapper;
import com.sap.exercise.util.DateHandler;
import com.sap.exercise.model.Event;

import java.util.Observable;
import java.util.Observer;

public class DeletionTimeFrameObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
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
