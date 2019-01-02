package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;

import java.util.Observable;
import java.util.Observer;

public abstract class AbstractEventsHandler<T> extends Observable implements EventsHandler<T> {

    protected final ThreadPoolHandler thPool = new ThreadPoolHandler();
    protected final EventsMapHandler mapHandler = new EventsMapHandler();

    {
        thPool.submit(DatabaseUtilFactory::createDbClient);
    }

    AbstractEventsHandler(Observer o) {
        addObserver(o);
    }

    public ThreadPoolHandler getThPool() {
        return thPool;
    }

    public EventsMapHandler getMapHandler() {
        return mapHandler;
    }
}
