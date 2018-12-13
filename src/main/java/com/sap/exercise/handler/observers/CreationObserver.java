package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.DateHandler;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class CreationObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventHandler handler = (EventHandler) observable;
        EventHandler.ActionType type = (EventHandler.ActionType) objects[1];

        if (type == EventHandler.ActionType.CREATE) {
            Serializable id = (Serializable) objects[2];

            handler.iterateEventsMap((cal, eventSet) -> {
                if (DateUtils.isSameDay(new DateHandler(DateHandler.Dates.TODAY).asCalendar(), cal)) {
                    event.setId((Integer) id);
                    eventSet.add(event);
                }
            });
        }
    }
}
