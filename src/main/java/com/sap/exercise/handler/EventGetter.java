package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtil;
import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
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
        DatabaseUtil db = DatabaseUtilFactory.getDb();
        db.beginTransaction();

        return db.getObjectWithRetry(s -> s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'"
                + var + "\' LIMIT 1;", Event.class)
                .uniqueResultOptional(), 3)
                .orElseThrow(() -> new NoSuchElementException("Invalid event name"));
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
            if ((ev = SharedResourcesFactory.getMapHandler().getFromMap(date)) == null) {
                listConsumer.accept(date);
            } else {
                events.addAll(ev);
            }
        };
    }

    private boolean setEventsInTable(String start, String end) {
        DatabaseUtil db = DatabaseUtilFactory.getDb();
        db.beginTransaction();

        List<CalendarEvents> list = db.getObjectWithRetry(s ->
                s.createNativeQuery("SELECT * FROM CalendarEvents WHERE Date >= \'" + start +
                "\' AND Date <= \'" + end + "\';", CalendarEvents.class).getResultList(), 3);

        ConcurrentMap<Calendar, Set<Event>> map = list.stream()
                .map(calEvents -> {
                    db.beginTransaction();
                    Event event = db.getObjectWithRetry(s -> s.get(Event.class, calEvents.getEventId()), 2);
                    event.setTimeOf(calEvents.getDate());
                    return event;
                })
                .collect(Collectors.groupingByConcurrent(Event::getTimeOf, Collectors.toSet()));

        boolean hasNewEntries = false;

        for (Calendar date : new DateHandler(start, end).fromTo()) {
            for (ConcurrentMap.Entry<Calendar, Set<Event>> entry : map.entrySet()) {
                if (DateUtils.isSameDay(date, entry.getKey())) {
                    SharedResourcesFactory.getMapHandler().putInMap(date, entry.getValue());
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
}
