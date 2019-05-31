package com.sap.exercise.listeners;

import com.sap.exercise.handler.ListenableEventType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ListenableEvent {

    private Map<ListenableEventType, EventListener> listenerMap;

    protected ListenableEvent() {
        listenerMap = Collections.synchronizedMap(new HashMap<>());
    }

    protected void addListener(ListenableEventType type, EventListener listener) {
        listenerMap.put(type, listener);
    }

    protected void notifyListeners(ListenableEventType type, Object... args) {
        listenerMap.get(type).notify(args);
    }
}
