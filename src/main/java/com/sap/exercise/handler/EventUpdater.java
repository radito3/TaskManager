package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtil;
import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.handler.observers.UpdateObserver;
import com.sap.exercise.model.Event;

public class EventUpdater extends AbstractEventsHandler<Event> implements EventsHandler<Event> {

    public EventUpdater() {
        super(new UpdateObserver());
    }

    @Override
    public void execute(Event event) {
        SharedResourcesFactory.getService()
                .execute(() -> {
                    DatabaseUtil db = DatabaseUtilFactory.getDb();
                    db.beginTransaction();
                    db.addOperation(s -> s.update(event));
                    db.commitTransaction();
                });
        setChanged();
        notifyObservers(event);
    }
}
