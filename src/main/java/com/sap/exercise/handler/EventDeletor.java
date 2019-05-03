package com.sap.exercise.handler;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.handler.observers.DeletionObserver;
import com.sap.exercise.model.Event;

public class EventDeletor extends AbstractEventsHandler<Event> implements EventsHandler<Event> {

    public EventDeletor(ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        super(new DeletionObserver(), thPool, mapHandler);
    }

    @Override
    public void execute(Event event) {
        thPool.submit(() -> new CRUDOperations<>(Event.class).delete(event));
        setChanged();
        notifyObservers(new Object[] { event });
    }
}
