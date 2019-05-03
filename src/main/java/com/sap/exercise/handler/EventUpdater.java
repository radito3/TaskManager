package com.sap.exercise.handler;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.handler.observers.UpdateObserver;
import com.sap.exercise.model.Event;

public class EventUpdater extends AbstractEventsHandler<Event> implements EventsHandler<Event> {

    public EventUpdater(ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        super(new UpdateObserver(), thPool, mapHandler);
    }

    @Override
    public void execute(Event event) {
        thPool.submit(() -> new CRUDOperations<>(Event.class).update(event));
        setChanged();
        notifyObservers(new Object[] { event });
    }
}
