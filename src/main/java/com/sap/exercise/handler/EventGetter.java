package com.sap.exercise.handler;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.db.CRUDOps;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventGetter extends AbstractEventsHandler<Event> implements EventsGetterHandler {

    public EventGetter() {
        super((obs, o) -> {});
    }

    @Override
    public Event getEventByTitle(String var) {
        return new CRUDOperations<>(Event.class)
                .getObjByProperty("Title", var)
                .orElseThrow(() -> new NullPointerException("Invalid event name"));
    }

    @Override
    public Set<Event> getEventsInTimeFrame(String start, String end) {
        Set<Event> events = new HashSet<>();
        List<String> nullDates = new ArrayList<>();

        new DateHandler(start, end).fromTo()
                .forEach(handleDates(events, date -> nullDates.add(new DateHandler(date).asString())));

        if (nullDates.size() != 0) {
            String startIndex = nullDates.get(0),
                    endIndex = nullDates.get(nullDates.size() - 1);
            if (setEventsInTable(startIndex, endIndex)) {
                new DateHandler(startIndex, endIndex).fromTo().forEach(handleDates(events, date -> {}));
            }
        }

        return events;
    }

    private Consumer<Calendar> handleDates(Set<Event> events, Consumer<Calendar> listConsumer) {
        return (date) -> {
            Set<Event> ev;
            if ((ev = mapHandler.getFromMap(date)) == null) {
                listConsumer.accept(date);
            } else {
                events.addAll(ev);
            }
        };
    }

    private boolean setEventsInTable(String start, String end) {
        CRUDOps<Event> crudOps = new CRUDOperations<>(Event.class);

        ConcurrentMap<Calendar, Set<Event>> map = crudOps.getEventsInTimeFrame(start, end)
                .stream()
                .map(calEvents -> {
                    Event event = crudOps.getObjById(calEvents.getEventId());
                    event.setTimeOf(calEvents.getDate());
                    return event;
                })
                .collect(Collectors.groupingByConcurrent(Event::getTimeOf, Collectors.toSet()));

        boolean hasNewEntries = false;

        for (Calendar date : new DateHandler(start, end).fromTo()) {
            for (ConcurrentMap.Entry<Calendar, Set<Event>> entry : map.entrySet()) {
                if (DateUtils.isSameDay(date, entry.getKey())) {
                    mapHandler.putInMap(date, entry.getValue());

                    entry.getValue().forEach(event -> {
                        if (DateUtils.isSameDay(new DateHandler(DateHandler.Dates.TODAY).asCalendar(), event.getTimeOf())) {
                            event.startNotification();
                            thPool.submit(event.getNotification());
                        }
                    });

                    hasNewEntries = true;
                }
            }
        }
        return hasNewEntries;
    }

    @Override
    public void execute(Event var) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ActionType<?> getActionType() {
        return null;
    }
}
