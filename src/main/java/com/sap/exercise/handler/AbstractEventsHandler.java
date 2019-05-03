package com.sap.exercise.handler;

import java.util.Observable;
import java.util.Observer;

abstract class AbstractEventsHandler<T> extends Observable implements EventsHandler<T> {

    AbstractEventsHandler(Observer o) {
        addObserver(o);
    }

    //TODO need to provide an exception throwing impl to all event handler methods
}
