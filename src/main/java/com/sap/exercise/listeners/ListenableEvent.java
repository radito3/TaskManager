package com.sap.exercise.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class ListenableEvent {

    private Collection<EventListener> listeners;

    protected ListenableEvent() {
        listeners = Collections.synchronizedCollection(new ArrayList<>(1));
    }

    protected void addListener(EventListener listener) {
        listeners.add(listener);
    }

    protected void notifyListeners(Object... args) {
        listeners.forEach(listener -> listener.notify(args));
    }
}
