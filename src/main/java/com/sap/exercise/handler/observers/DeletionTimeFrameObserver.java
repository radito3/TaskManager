package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.util.DateHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class DeletionTimeFrameObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        String[] timeFrame = (String[]) objects[1];
        DateHandler dateHandler = new DateHandler(timeFrame[0], timeFrame[1]);

        SharedResourcesFactory.getService().execute(() -> {
            for (Calendar cal : dateHandler.fromTo()) {
                SharedResourcesFactory.getMapHandler().iterateEventsMap((date, eventSet) -> {
                    if (DateUtils.isSameDay(cal, date.getCalendar())) {
                        eventSet.removeIf(event1 -> event1.getId().equals(event.getId()));
                    }
                });
            }
        });
    }
}
