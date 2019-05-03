package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.AbstractEventsHandler;
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
        AbstractEventsHandler<?> handler = (AbstractEventsHandler<?>) observable;
        String[] timeFrame = (String[]) objects[1];
        DateHandler dateHandler = new DateHandler(timeFrame[0], timeFrame[1]);

        handler.getThPool().submit(() -> {
            if (dateHandler.containsToday()) {
                handler.getThPool().submit(event::deleteNotification);
            }
        });
        handler.getThPool().submit(() -> {
            for (Calendar cal : dateHandler.fromTo()) {
                handler.getMapHandler().iterateEventsMap((date, eventSet) -> {
                    if (DateUtils.isSameDay(cal, date)) {
                        eventSet.removeIf(event1 -> event1.getId().equals(event.getId()));
                    }
                });
            }
        });
    }
}
