package com.sap.exercise.handler;

import com.sap.exercise.listeners.DeletionInTimeFrameListener;
import com.sap.exercise.listeners.DeletionListener;
import com.sap.exercise.persistence.DatabaseUtilFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;

public class EventDeletor extends AbstractEventsHandler<Event> {

    private String startDate, endDate;
    private boolean isInTimeFrame;

    public EventDeletor(boolean condition, String startDate, String endDate) {
        super(condition ? new DeletionInTimeFrameListener() : new DeletionListener());
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
                        .beginTransaction()
                        .addOperation(s -> s.delete(event))
                        .commit());
        notifyListeners(event);
    }

    private void deleteMultipleEntries(Event event) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> DatabaseUtilFactory.getDatabaseUtil()
                        .beginTransaction()
                        .addOperation(s -> s.createNativeQuery("DELETE FROM CalendarEvents WHERE EventId = "
                                + event.getId() + " AND Date >= \'" + startDate + "\' AND Date <= \'" + endDate + "\';")
                                .executeUpdate())
                        .commit());
        notifyListeners(new Object[] { event, new String[] {startDate, endDate} });
    }
}
