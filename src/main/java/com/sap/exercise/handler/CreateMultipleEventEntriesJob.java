package com.sap.exercise.handler;

import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.persistence.TransactionBuilder;

import java.util.Calendar;
import java.util.stream.IntStream;

class CreateMultipleEventEntriesJob implements Runnable {

    private final int id;
    private final Event event;

    CreateMultipleEventEntriesJob(int id, Event event) {
        this.id = id;
        this.event = event;
    }

    @Override
    public void run() {
        TransactionBuilder transaction = TransactionBuilder.newInstance();

        IntStream.iterate(1, i -> i + 1)
            .mapToObj(this::mapToCalEvents)
            .limit(event.getToRepeat() == Event.RepeatableType.YEARLY ? 4 : 30)
            .forEach(calEvents -> transaction.addOperation(s -> s.save(calEvents)));
        
        transaction.commit();
    }
    
    private CalendarEvents mapToCalEvents(int i) {
        Calendar calendar = (Calendar) event.getTimeOf().clone();
        calendar.add(getCalendarField(), i);
        return new CalendarEvents(id, calendar);
    }
    
    private int getCalendarField() {
        switch (event.getToRepeat()) {
            case DAILY:
                return Calendar.DAY_OF_MONTH;
            case WEEKLY:
                return Calendar.WEEK_OF_YEAR;
            case MONTHLY:
                return Calendar.MONTH;
            case YEARLY:
                return Calendar.YEAR;
        }
        throw new IllegalStateException("Unknown repeatable type");
    }
}
