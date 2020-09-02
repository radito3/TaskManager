package com.sap.exercise.services;

import com.sap.exercise.model.Event;

import java.io.Closeable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class EventsCache implements Closeable {

    private final ConcurrentMap<LocalDate, Set<Event>> eventsMap;

    EventsCache() {
        eventsMap = new ConcurrentHashMap<>();
    }

    public void forAllEvents(Consumer<Set<Event>> consumer) {
        eventsMap.values().forEach(consumer);
    }

    public void put(LocalDate date, Event event) {
        eventsMap.computeIfAbsent(date, k -> new HashSet<>())
                 .add(event);
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
        eventsMap.values().removeIf(Set::isEmpty);
    }

    @Override
    public void close() {
        eventsMap.clear();
    }
}
