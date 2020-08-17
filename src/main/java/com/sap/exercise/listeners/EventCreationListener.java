package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;

import java.time.LocalDate;

public class EventCreationListener implements EventListener {

    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];
        Integer id = (Integer) args[1];

        event.setId(id);
        LocalDate date = event.getTimeOf().toLocalDate();

        SharedResourcesFactory.getEventsCache()
                              .put(date, event);
    }
}
