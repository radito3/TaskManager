package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtil;
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
                .execute(() -> {
                    DatabaseUtil db = DatabaseUtilFactory.getDb();
                    db.beginTransaction();
                    db.addOperation(s -> s.createNativeQuery("DELETE FROM CalendarEvents WHERE EventId = "
                            + event.getId() + " AND Date >= \'" + start + "\' AND Date <= \'" + end + "\';")
                            .executeUpdate());
                    db.commitTransaction();
                });
        setChanged();
        notifyObservers(new Object[] { event, new String[] {start, end} });
    }

    @Override
    public void execute(Event var) {
        throw new UnsupportedOperationException();
    }
}
