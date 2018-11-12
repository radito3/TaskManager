package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

import java.io.Serializable;
import java.util.Observable;
import java.util.concurrent.Future;

class EventActions extends Observable {

    enum ActionType {
        CREATE, UPDATE, DELETE, DELETE_TIME_FRAME
    }

    void onCreate(Event event, Future<Serializable> future) {
        setChanged();
        notifyObservers(new Object[] { event, ActionType.CREATE, future });
    }

    void onUpdate(Event event) {
        setChanged();
        notifyObservers(new Object[] { event, ActionType.UPDATE });
    }

    void onDelete(Event event) {
        setChanged();
        notifyObservers(new Object[] { event, ActionType.DELETE });
    }

    void onDeleteTimeFrame(Event event, String start, String end) {
        setChanged();
        notifyObservers(new Object[] { event, ActionType.DELETE_TIME_FRAME, new String[] {start, end} });
    }
}
