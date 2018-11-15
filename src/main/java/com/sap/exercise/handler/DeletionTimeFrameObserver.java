package com.sap.exercise.handler;

import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class DeletionTimeFrameObserver implements Observer {

    private Map<Calendar, Set<Event>> table;
    private ExecutorService service;

    DeletionTimeFrameObserver(Map<Calendar, Set<Event>> table, ExecutorService service) {
        this.table = table;
        this.service = service;
    }

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventActions.ActionType type = (EventActions.ActionType) objects[1];

        if (type == EventActions.ActionType.DELETE_TIME_FRAME) {
            String[] timeFrame = (String[]) objects[2];

            service.submit(() -> {
                if (DateHandler.containsToday(timeFrame[0], timeFrame[1])) {
                    service.submit(Notifications.onDelete(event));
                }
            });
            service.submit(() -> {
                for (Calendar cal : DateHandler.fromTo(timeFrame[0], timeFrame[1])) {
                    table.forEach((date, eventSet) -> {
                        if (DateUtils.isSameDay(cal, date)) {
                            eventSet.removeIf(event1 -> event1.equals(event));
                        }
                    });
                }
            });
        }
    }
}
