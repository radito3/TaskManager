package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.AbstractEventsHandler;
import com.sap.exercise.util.DateHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

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
        AbstractEventsHandler<?> handler = (AbstractEventsHandler<?>) observable;
        Serializable id = (Serializable) objects[1];
        event.setId((Integer) id);

        Calendar cal = event.getTimeOf();
        if (DateUtils.isSameDay(new DateHandler(DateHandler.Dates.TODAY).asCalendar(), cal)) {
            event.startNotification();
            handler.getThPool().submit(event.getNotification());
        }

        Set<Event> events;
        if ((events = handler.getMapHandler().getFromMap(cal)) != null) {
            events.add(event);
        } else {
            handler.getMapHandler().putInMap(cal, new HashSet<>(Collections.singleton(event)));
        }
    }
}
