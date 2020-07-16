package com.sap.exercise.handler;

import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.persistence.TransactionBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
        transaction.addOperation(session -> {
            int limit = event.getToRepeat() == Event.RepeatableType.YEARLY ? 4 : 30;
            for (int i = 1; i < limit; i++) {
                session.save(createCalEventEntry(i));
            }
        });
        transaction.commit();
    }

    private CalendarEvents createCalEventEntry(int num) {
        LocalDate date = LocalDate.from(event.getTimeOf().plus(num, getChronoUnit()));
        return new CalendarEvents(id, date);
    }
    
    private ChronoUnit getChronoUnit() {
        switch (event.getToRepeat()) {
            case DAILY:
                return ChronoUnit.DAYS;
            case WEEKLY:
                return ChronoUnit.WEEKS;
            case MONTHLY:
                return ChronoUnit.MONTHS;
            case YEARLY:
                return ChronoUnit.YEARS;
        }
        throw new IllegalStateException("Unknown repeatable type");
    }
}
