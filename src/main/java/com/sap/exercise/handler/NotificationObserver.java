package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

import java.util.Observable;
import java.util.Observer;

//this will be removed
public class NotificationObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventObservable.ActionType type = (EventObservable.ActionType) objects[1];
        String[] timeFrame = objects.length == 3 ? (String[]) objects[2] : new String[] {};
        switch (type) { //need to split this between observers
            case CREATE:
                EventHandler.checkForUpcomingEvents();
                break;
            case UPDATE:
                NotificationHandler.onDelete(event).run();
                EventHandler.checkForUpcomingEvents();
                break;
            case DELETE:
                NotificationHandler.onDelete(event).run();
                break;
            case DELETE_TIME_FRAME:

        }
    }
}
