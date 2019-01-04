package com.sap.exercise.handler;

import java.util.Observable;
import java.util.Observer;

public abstract class AbstractEventsHandler<T> extends Observable implements EventsHandler<T> {

    protected final ThreadPoolHandler thPool;
    protected final EventsMapHandler mapHandler;

    AbstractEventsHandler(Observer o, ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        addObserver(o);
        this.thPool = thPool;
        this.mapHandler = mapHandler;
    }

    public ThreadPoolHandler getThPool() {
        return thPool;
    }

    public EventsMapHandler getMapHandler() {
        return mapHandler;
    }
}
