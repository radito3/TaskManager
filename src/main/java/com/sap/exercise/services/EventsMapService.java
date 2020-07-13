package com.sap.exercise.services;

import com.sap.exercise.model.Event;
import com.sap.exercise.util.SimplifiedCalendar;

import java.io.Closeable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public final class EventsMapService implements Closeable {

    private final ConcurrentMap<SimplifiedCalendar, Set<Event>> eventsMap;

    EventsMapService() {
        eventsMap = new ConcurrentHashMap<>();
    }

    public void iterateEventsMap(BiConsumer<SimplifiedCalendar, Set<Event>> biConsumer) {
        eventsMap.forEach(biConsumer);
        eventsMap.values().removeIf(Collection::isEmpty);
    }

    public void putInMap(SimplifiedCalendar calendar, Set<Event> events) {
        eventsMap.put(calendar, events);
    }

    public Set<Event> getFromMap(SimplifiedCalendar calendar) {
        return eventsMap.get(calendar);
    }

    @Override
    public void close() {
        eventsMap.clear();
    }
}
