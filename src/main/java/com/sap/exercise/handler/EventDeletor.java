package com.sap.exercise.handler;

import com.sap.exercise.persistence.DatabaseUtilFactory;
import com.sap.exercise.handler.observers.DeletionObserver;
import com.sap.exercise.handler.observers.DeletionTimeFrameObserver;
import com.sap.exercise.model.Event;

public class EventDeletor extends AbstractEventsHandler<Event> {

    private String startDate, endDate;
    private boolean isInTimeFrame;

    public EventDeletor(boolean condition, String startDate, String endDate) {
        super(condition ? new DeletionTimeFrameObserver() : new DeletionObserver());
        this.startDate = startDate;
        this.endDate = endDate;
        this.isInTimeFrame = condition;
    }

    @Override
    public void execute(Event event) {
        if (isInTimeFrame)
            deleteMultipleEntries(event);
        else
            deleteSingleEntry(event);
    }

    private void deleteSingleEntry(Event event) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> DatabaseUtilFactory.getDatabaseUtil()
                        .createTransactionBuilder()
                        .addOperation(s -> s.delete(event))
                        .build());
        setChanged();
        notifyObservers(event);
    }

    private void deleteMultipleEntries(Event event) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> DatabaseUtilFactory.getDatabaseUtil()
                        .createTransactionBuilder()
                        .addOperation(s -> s.createNativeQuery("DELETE FROM CalendarEvents WHERE EventId = "
                                + event.getId() + " AND Date >= \'" + startDate + "\' AND Date <= \'" + endDate + "\';")
                                .executeUpdate())
                        .build());
        setChanged();
        notifyObservers(new Object[] { event, new String[] {startDate, endDate} });
    }
}
