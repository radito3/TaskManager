package com.sap.exercise.listeners;

import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateParser;

import java.time.LocalDate;

public class EventDeletionInTimeFrameListener implements EventListener {
    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];

        SharedResourcesFactory.getAsyncExecutionsService()
                  .execute(() -> {
            for (LocalDate date : DateParser.getRangeBetween((String) args[1], (String) args[2])) {
                SharedResourcesFactory.getEventsCache()
                          .forEach((localDate, eventSet) -> {
                    if (date.equals(localDate)) {
                        eventSet.removeIf(event1 -> event1.getId().equals(event.getId()));
                    }
                });
            }
        });
    }
}
