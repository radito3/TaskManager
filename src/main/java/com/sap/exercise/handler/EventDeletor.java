package com.sap.exercise.handler;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.handler.observers.DeletionObserver;
import com.sap.exercise.model.Event;

public class EventDeletor extends AbstractEventsHandler<Event> implements EventsHandler<Event> {

    public EventDeletor() {
        super(new DeletionObserver());
    }

    @Override
    public void execute(Event event) {
        SharedResourcesFactory.getService().execute(() -> new CRUDOperations<>(Event.class).delete(event));
        setChanged();
        notifyObservers(event);
    }
}
