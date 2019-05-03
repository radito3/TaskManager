package com.sap.exercise.handler;

import com.sap.exercise.model.Event;

import java.io.Closeable;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public class EventsMapHandler implements Closeable {

    private final ConcurrentMap<Calendar, Set<Event>> eventsMap = new ConcurrentHashMap<>();

    public void iterateEventsMap(BiConsumer<Calendar, Set<Event>> biConsumer) {
        eventsMap.forEach(biConsumer);
    }

    public void putInMap(Calendar calendar, Set<Event> events) {
        eventsMap.put(calendar, events);
    }

    public Set<Event> getFromMap(Calendar calendar) {
        return eventsMap.get(calendar);
    }

    @Override
    public void close() {
        eventsMap.clear();
    }
}
