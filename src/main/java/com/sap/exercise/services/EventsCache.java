package com.sap.exercise.services;

import com.sap.exercise.model.Event;

import java.io.Closeable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class EventsCache implements Closeable {

    private final ConcurrentMap<LocalDate, Set<Event>> eventsMap;

    EventsCache() {
        eventsMap = new ConcurrentHashMap<>();
    }

    public void forEach(BiConsumer<LocalDate, Set<Event>> biConsumer) {
        eventsMap.forEach(biConsumer);
        eventsMap.values().removeIf(Collection::isEmpty);
    }

    public void put(LocalDate date, Set<Event> events) {
        if (eventsMap.containsKey(date)) {
            eventsMap.get(date).addAll(events);
        } else {
            eventsMap.put(date, events);
        }
    }

    public void putAll(Map<LocalDate, Set<Event>> map) {
        for (Map.Entry<LocalDate, Set<Event>> entry : map.entrySet()) {
            eventsMap.merge(entry.getKey(), entry.getValue(), (presentEvents, newEvents) -> {
                presentEvents.addAll(newEvents);
                return presentEvents;
            });
        }
    }

    public Set<Event> get(LocalDate date) {
        return eventsMap.get(date);
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
