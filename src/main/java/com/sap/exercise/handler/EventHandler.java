package com.sap.exercise.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Observable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.db.CRUDOps;
import com.sap.exercise.handler.observers.CreationObserver;
import com.sap.exercise.handler.observers.DeletionObserver;
import com.sap.exercise.handler.observers.DeletionTimeFrameObserver;
import com.sap.exercise.handler.observers.UpdateObserver;
import org.apache.commons.lang3.time.DateUtils;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;

public class EventHandler extends Observable {

    private final ThreadPoolHandler thPool = new ThreadPoolHandler();
    private final Map<Calendar, Set<Event>> eventsMap = new Hashtable<>();

    public enum ActionType {
        CREATE, UPDATE, DELETE, DELETE_TIME_FRAME
    }

    public EventHandler() {
        thPool.submitRunnable(DatabaseUtilFactory::createDbClient);

        addObserver(new CreationObserver());
        addObserver(new UpdateObserver());
        addObserver(new DeletionObserver());
        addObserver(new DeletionTimeFrameObserver());

        String date = new DateHandler(DateHandler.Dates.TODAY).asString();
        thPool.submitRunnable(() -> setEventsInTable(date, date));
    }

    public void create(Event event) {
        CRUDOps<Event> crudOps1 = new CRUDOperations<>(Event.class);
        CRUDOps<CalendarEvents> crudOps2 = new CRUDOperations<>(CalendarEvents.class);

        Serializable id = crudOps1.create(event);
        thPool.submitCallable(() -> crudOps2.create(new CalendarEvents((Integer) id, event.getTimeOf())));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            thPool.submitRunnable(() -> crudOps2.create(eventsList((Integer) id, event)));
        }

        setChanged();
        notifyObservers(new Object[] { event, ActionType.CREATE, id });
    }

    private List<CalendarEvents> eventsList(Integer eventId, Event event) {
        switch (event.getToRepeat()) {
            case DAILY:
                return eventEntriesHandler(30, eventId, event, Calendar.DAY_OF_MONTH);
            case WEEKLY:
                return eventEntriesHandler(30, eventId, event, Calendar.WEEK_OF_YEAR);
            case MONTHLY:
                return eventEntriesHandler(30, eventId, event, Calendar.MONTH);
            default:
                return eventEntriesHandler(4, eventId, event, Calendar.YEAR);
        }
    }

    private List<CalendarEvents> eventEntriesHandler(int endInclusive, Integer eventId, Event event, int field) {
        return IntStream.rangeClosed(1, endInclusive)
                .mapToObj(i -> {
                    Calendar calendar = (Calendar) event.getTimeOf().clone();
                    calendar.add(field, i);
                    return new CalendarEvents(eventId, calendar);
                })
                .collect(Collectors.toList());
    }

    public void update(Event event) {
        thPool.submitRunnable(() -> new CRUDOperations<>(Event.class).update(event));
        setChanged();
        notifyObservers(new Object[] { event, ActionType.UPDATE });
    }

    public void delete(Event event) {
        thPool.submitRunnable(() -> new CRUDOperations<>(Event.class).delete(event));
        setChanged();
        notifyObservers(new Object[] { event, ActionType.DELETE });
    }

    public void deleteInTimeFrame(Event event, String start, String end) {
        thPool.submitRunnable(() -> new CRUDOperations<>(Event.class).deleteEventsInTimeFrame(event, start, end));
        setChanged();
        notifyObservers(new Object[] { event, ActionType.DELETE_TIME_FRAME, new String[] {start, end} });
    }

    public Event getEventByTitle(String title) {
        return new CRUDOperations<>(Event.class)
                .getObjByProperty("Title", title)
                .orElseThrow(() -> new NullPointerException("Invalid event name"));
    }

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
            if ((ev = eventsMap.get(date)) == null) {
                listConsumer.accept(date);
            } else {
                events.addAll(ev);
            }
        };
    }

    private boolean setEventsInTable(String start, String end) {
        CRUDOps<Event> crudOps = new CRUDOperations<>(Event.class);

        Map<Calendar, Set<Event>> map = crudOps.getEventsInTimeFrame(start, end)
                .stream()
                .map(calEvents -> {
                    Event event = crudOps.getObjById(calEvents.getEventId());
                    event.setTimeOf(calEvents.getDate());
                    return event;
                })
                .collect(Collectors.groupingBy(Event::getTimeOf, Collectors.toSet()));

        boolean hasNewEntries = false;

        for (Calendar date : new DateHandler(start, end).fromTo()) {
            for (Map.Entry<Calendar, Set<Event>> entry : map.entrySet()) {
                if (DateUtils.isSameDay(date, entry.getKey())) {
                    eventsMap.put(date, entry.getValue());

                    entry.getValue().forEach(event -> {
                        if (DateUtils.isSameDay(new DateHandler(DateHandler.Dates.TODAY).asCalendar(), event.getTimeOf())) {
                            event.startNotification();
                            thPool.submitRunnable(event.getNotification());
                        }
                    });

                    hasNewEntries = true;
                }
            }
        }
        return hasNewEntries;
    }

    public ThreadPoolHandler getThPool() {
        return thPool;
    }

    public void iterateEventsMap(BiConsumer<Calendar, Set<Event>> biConsumer) {
        eventsMap.forEach(biConsumer);
    }

    public void putInMap(Calendar calendar, Set<Event> events) {
        eventsMap.put(calendar, events);
    }

    public Set<Event> getFromMap(Calendar calendar) {
        return eventsMap.get(calendar);
    }
}
