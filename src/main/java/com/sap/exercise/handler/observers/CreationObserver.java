package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.model.Event;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class CreationObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        Serializable id = (Serializable) objects[1];

        event.setId((Integer) id);
        Calendar cal = event.getTimeOf();

        Set<Event> events;
        if ((events = SharedResourcesFactory.getMapHandler().getFromMap(cal)) != null) {
            events.add(event);
        } else {
            SharedResourcesFactory.getMapHandler().putInMap(cal, new HashSet<>(Collections.singleton(event)));
        }
    }
}
