package com.sap.exercise.services;

import com.sap.exercise.model.Event;
import com.sap.exercise.util.CalendarWrapper;

import java.io.Closeable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public final class EventsMapService implements Closeable {

    private final ConcurrentMap<CalendarWrapper, Set<Event>> eventsMap;

    EventsMapService() {
        eventsMap = new ConcurrentHashMap<>();
    }

    public void iterateEventsMap(BiConsumer<CalendarWrapper, Set<Event>> biConsumer) {
        eventsMap.forEach(biConsumer);
        eventsMap.values().removeIf(Collection::isEmpty);
    }

    public void putInMap(CalendarWrapper calendar, Set<Event> events) {
        eventsMap.put(calendar, events);
    }

    public Set<Event> getFromMap(CalendarWrapper calendar) {
        return eventsMap.get(calendar);
    }

    @Override
    public void close() {
        eventsMap.clear();
    }
}
