package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

import java.util.Calendar;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class UpdateObserver implements Observer {

    private Map<Calendar, Set<Event>> table;

    UpdateObserver(Map<Calendar, Set<Event>> table) {
        this.table = table;
    }

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventActions.ActionType type = (EventActions.ActionType) objects[1];

        if (type == EventActions.ActionType.UPDATE) {
            NotificationHandler.onDelete(event).run();

            table.values().forEach(set -> {
                if (set.removeIf(event1 -> event1.getId().equals(event.getId()))) {
                    set.add(event);
                }
            });
        }
    }
}
