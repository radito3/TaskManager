package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

import java.util.Calendar;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class DeletionObserver implements Observer {

    private Map<Calendar, Set<Event>> table;
    private ExecutorService service;

    DeletionObserver(Map<Calendar, Set<Event>> table, ExecutorService service) {
        this.table = table;
        this.service = service;
    }

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventActions.ActionType type = (EventActions.ActionType) objects[1];

        if (type == EventActions.ActionType.DELETE) {
            service.submit(NotificationHandler.onDelete(event));
            service.submit(() -> table.values().forEach(set -> set.removeIf(event1 -> event1.equals(event))));
        }
    }
}
