package com.sap.exercise.listeners;

import com.sap.exercise.services.EventsCache;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateParser;

import java.time.LocalDate;

public class EventDeletionInTimeFrameListener implements EventListener {

    @Override
    public void notify(Object... args) {
        Event event = (Event) args[0];
        String startDate = (String) args[1];
        String endDate = (String) args[2];

        SharedResourcesFactory.getAsyncExecutionsService()
                              .execute(() -> deleteEventsInTimeFrame(startDate, endDate, event));
    }

    private void deleteEventsInTimeFrame(String startDate, String endDate, Event event) {
        EventsCache events = SharedResourcesFactory.getEventsCache();

        for (LocalDate date : DateParser.getRangeBetween(startDate, endDate)) {
            events.remove(event1 -> event1.getTimeOf().toLocalDate().equals(date) &&
                                    event1.getId().equals(event.getId()));
        }
    }
}
