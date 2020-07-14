package com.sap.exercise.services;

import com.sap.exercise.model.Event;
import com.sap.exercise.util.SimplifiedCalendar;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class EventsCache implements Closeable {

    private final ConcurrentMap<SimplifiedCalendar, Set<Event>> eventsMap;

    EventsCache() {
        eventsMap = new ConcurrentHashMap<>();
    }

    public void forEach(BiConsumer<SimplifiedCalendar, Set<Event>> biConsumer) {
        eventsMap.forEach(biConsumer);
        eventsMap.values().removeIf(Collection::isEmpty);
    }

    public void put(SimplifiedCalendar calendar, Set<Event> events) {
        eventsMap.put(calendar, events);
    }

    public void putAll(Map<SimplifiedCalendar, Set<Event>> map) {
        eventsMap.putAll(map);
    }

    public Set<Event> get(SimplifiedCalendar calendar) {
        return eventsMap.get(calendar);
    }

    public void remove(Predicate<Event> condition) {
        for (Set<Event> events : eventsMap.values()) {
            events.removeIf(condition);
        }
        eventsMap.values().removeIf(Collection::isEmpty);
    }

    @Override
    public void close() {
        eventsMap.clear();
    }
}
