package com.sap.exercise.handler;

import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.persistence.TransactionBuilder;
import com.sap.exercise.persistence.TransactionBuilderFactory;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CreateMultipleEventEntries implements Runnable {

    private final int id;
    private final Event event;

    CreateMultipleEventEntries(int id, Event event) {
        this.id = id;
        this.event = event;
    }

    @Override
    public void run() {
        TransactionBuilder transaction = TransactionBuilderFactory.getTransactionBuilder();
        eventsList(id, event)
                .forEach(calEvents -> transaction.addOperation(s -> s.save(calEvents)));
        transaction.commit();
    }

    private List<CalendarEvents> eventsList(Integer eventId, Event event) {
        switch (event.getToRepeat()) {
            case DAILY:
                return handleEventEntries(30, eventId, event, Calendar.DAY_OF_MONTH);
            case WEEKLY:
                return handleEventEntries(30, eventId, event, Calendar.WEEK_OF_YEAR);
            case MONTHLY:
                return handleEventEntries(30, eventId, event, Calendar.MONTH);
            case YEARLY:
                return handleEventEntries(4, eventId, event, Calendar.YEAR);
        }
        throw new IllegalStateException("Unknown event repeatable type");
    }

    private List<CalendarEvents> handleEventEntries(int endInclusive, Integer eventId, Event event, int field) {
        return IntStream.rangeClosed(1, endInclusive)
                .mapToObj(i -> {
                    Calendar calendar = (Calendar) event.getTimeOf().clone();
                    calendar.add(field, i);
                    return new CalendarEvents(eventId, calendar);
                })
                .collect(Collectors.toList());
    }
}