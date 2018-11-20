package com.sap.exercise.handler;

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
        EventHandler handler = (EventHandler) observable;
        EventHandler.ActionType type = (EventHandler.ActionType) objects[1];

        if (type == EventHandler.ActionType.DELETE_TIME_FRAME) {
            String[] timeFrame = (String[]) objects[2];

            handler.submitRunnable(() -> {
                if (DateHandler.containsToday(timeFrame[0], timeFrame[1])) {
                    handler.submitRunnable(Notifications.onDelete(event));
                }
            });
            handler.submitRunnable(() -> {
                for (Calendar cal : DateHandler.fromTo(timeFrame[0], timeFrame[1])) {
                    handler.iterateEventsMap((date, eventSet) -> {
                        if (DateUtils.isSameDay(cal, date)) {
                            eventSet.removeIf(event1 -> event1.equals(event));
                        }
                    });
                }
            });
        }
    }
}
