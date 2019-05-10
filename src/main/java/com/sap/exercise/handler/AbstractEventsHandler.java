package com.sap.exercise.handler;

import java.util.Observable;
import java.util.Observer;

abstract class AbstractEventsHandler<T> extends Observable implements EventsHandler<T> {

    AbstractEventsHandler(Observer o) {
        addObserver(o);
    }

    @Override
    public void execute(T var) {
        throw new UnsupportedOperationException();
    }
}
