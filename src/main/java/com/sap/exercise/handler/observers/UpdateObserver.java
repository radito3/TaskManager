package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;

import java.util.Observable;
import java.util.Observer;

public class UpdateObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventHandler handler = (EventHandler) observable;
        EventHandler.ActionType type = (EventHandler.ActionType) objects[1];

        if (type == EventHandler.ActionType.UPDATE) {
            event.deleteNotification();
            event.startNotification();
            handler.getThPool().submit(event.getNotification());

            handler.getMapHandler().iterateEventsMap((cal, set) -> {
                if (set.removeIf(event1 -> event1.getId().equals(event.getId()))) {
                    set.add(event);
                }
            });
        }
    }
}
