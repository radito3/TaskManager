package com.sap.exercise.listeners;

import com.sap.exercise.handler.ListenableEventType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ListenableEvent {

    private ConcurrentMap<ListenableEventType, EventListener> listenerMap;

    protected ListenableEvent() {
        listenerMap = new ConcurrentHashMap<>();
    }

    protected void addListener(ListenableEventType type, EventListener listener) {
        listenerMap.put(type, listener);
    }

    protected void notifyListeners(ListenableEventType type, Object... args) {
        listenerMap.get(type).notify(args);
    }
}
