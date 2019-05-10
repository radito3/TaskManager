package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.handler.observers.DeletionTimeFrameObserver;
import com.sap.exercise.model.Event;

public class EventDeletorTF extends AbstractEventsHandler<Event> implements EventsDeletionTFHandler {

    public EventDeletorTF() {
        super(new DeletionTimeFrameObserver());
    }

    @Override
    public void execute(Event event, String start, String end) {
        SharedResourcesFactory.getService()
                .execute(() -> DatabaseUtilFactory.getDb()
                        .beginTransaction()
                        .addOperation(s -> s.createNativeQuery("DELETE FROM CalendarEvents WHERE EventId = "
                                + event.getId() + " AND Date >= \'" + start + "\' AND Date <= \'" + end + "\';")
                                .executeUpdate())
                        .commit());
        setChanged();
        notifyObservers(new Object[] { event, new String[] {start, end} });
    }
}
