package com.sap.exercise.handler;

import com.sap.exercise.listeners.EventListener;
import com.sap.exercise.listeners.ListenableEvent;

abstract class AbstractEventsHandler<T> extends ListenableEvent implements EventsHandler<T> {

    AbstractEventsHandler(EventListener listener) {
        addListener(listener);
    }

    AbstractEventsHandler() {
    }

    @Override
    public void execute(T var) {
        throw new UnsupportedOperationException();
    }
}
