package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.handler.Notifications;
import com.sap.exercise.model.Event;

import java.util.Observable;
import java.util.Observer;

public class DeletionObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventHandler handler = (EventHandler) observable;
        EventHandler.ActionType type = (EventHandler.ActionType) objects[1];

        if (type == EventHandler.ActionType.DELETE) {
            handler.getThPool().submitRunnable(Notifications.onDelete(event));
            handler.getThPool().submitRunnable(() -> handler.iterateEventsMap((cal, set) ->
                    set.removeIf(event1 -> event1.getId().equals(event.getId()))
            ));
        }
    }
}
