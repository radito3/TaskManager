package com.sap.exercise.handler;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.handler.observers.DeletionTimeFrameObserver;
import com.sap.exercise.model.Event;

public class EventDeletorTF extends AbstractEventsHandler<Event> implements EventsDeletionTFHandler {

    public EventDeletorTF(ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        super(new DeletionTimeFrameObserver(), thPool, mapHandler);
    }

    @Override
    public void execute(Event event, String start, String end) {
        thPool.submit(() -> new CRUDOperations<>(Event.class).deleteEventsInTimeFrame(event, start, end));
        setChanged();
        notifyObservers(new Object[] { event, new String[] {start, end} });
    }

    @Override
    public void execute(Event var) {
        throw new UnsupportedOperationException();
    }
}
