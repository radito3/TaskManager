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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.DateUtils;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;

public class EventHandler extends Observable {

    private static EventHandler instance = new EventHandler();

    private final ExecutorService service = Executors.newCachedThreadPool();
    private final Map<Calendar, Set<Event>> eventsMap = new Hashtable<>();

    enum ActionType {
        CREATE, UPDATE, DELETE, DELETE_TIME_FRAME
    }

    EventHandler() {
        service.submit(DatabaseUtilFactory::createDbClient);
        addObserver(new CreationObserver());
        addObserver(new UpdateObserver());
        addObserver(new DeletionObserver());
        addObserver(new DeletionTimeFrameObserver());
        checkForUpcomingEvents();
    }

    public static EventHandler getInstance() {
        return instance;
    }

    private void checkForUpcomingEvents() {
        service.submit(() -> {
            Calendar today = Calendar.getInstance();
            int year = today.get(Calendar.YEAR);
            int month = today.get(Calendar.MONTH);
            int day = today.get(Calendar.DAY_OF_MONTH);
            String date = DateHandler.stringifyDate(year, month, day);

            Set<Event> events = getEventsInTimeFrame(date, date);
            if (!events.isEmpty()) {
                events.forEach(event -> service.submit(Notifications.newNotificationHandler(event)));
            }
        });
    }

    public void create(Event event) {
        Serializable id = CRUDOperations.create(event);
        service.submit(() -> CRUDOperations.create(new CalendarEvents((Integer) id, event.getTimeOf())));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            service.submit(() -> CRUDOperations.create(eventsList((Integer) id, event)));
        }

        setChanged();
        notifyObservers(new Object[] { event, ActionType.CREATE, id });
        checkForUpcomingEvents();
    }

    private static List<CalendarEvents> eventsList(Integer eventId, Event event) {
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

    private static List<CalendarEvents> eventEntriesHandler(int endInclusive, Integer eventId, Event event, int field) {
        Supplier<Calendar> calSupplier = () -> (Calendar) event.getTimeOf().clone();
        return IntStream.rangeClosed(1, endInclusive)
                .mapToObj(i -> {
                    Calendar calendar = calSupplier.get();
                    calendar.add(field, i);
                    return new CalendarEvents(eventId, calendar);
                })
                .collect(Collectors.toList());
    }

    public void update(Event event) {
        service.submit(() -> CRUDOperations.update(event));
        setChanged();
        notifyObservers(new Object[] { event, ActionType.UPDATE });
        checkForUpcomingEvents();
    }

    public void delete(Event event) {
        service.submit(() -> CRUDOperations.delete(event));
        setChanged();
        notifyObservers(new Object[] { event, ActionType.DELETE });
    }

    public void deleteInTimeFrame(Event event, String start, String end) {
        service.submit(() -> CRUDOperations.deleteEventsInTimeFrame(event, start, end));
        setChanged();
        notifyObservers(new Object[] { event, ActionType.DELETE_TIME_FRAME, new String[] {start, end} });
    }

    public Event getEventByTitle(String title) {
        return CRUDOperations.getEventByTitle(title)
                .orElseThrow(() -> new NullPointerException("Invalid event name"));
    }

    public Set<Event> getEventsInTimeFrame(String start, String end) {
        Set<Event> events = new HashSet<>();
        List<String> nullDates = new ArrayList<>();

        DateHandler.fromTo(start, end)
                .forEach(handleDates(events, date -> nullDates.add(DateHandler.asString(date))));

        if (nullDates.size() != 0) {
            String startIndex = nullDates.get(0),
                    endIndex = nullDates.get(nullDates.size() - 1);
            if (setEventsInTable(startIndex, endIndex)) {
                DateHandler.fromTo(startIndex, endIndex)
                        .forEach(handleDates(events, date -> {}));
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
        Map<Calendar, Set<Event>> map = CRUDOperations.getEventsInTimeFrame(start, end).stream()
                .map(calEvents -> {
                    Event event = CRUDOperations.getObjById(Event.class, calEvents.getEventId());
                    event.setTimeOf(calEvents.getDate());
                    return event;
                })
                .collect(Collectors.groupingBy(Event::getTimeOf, Collectors.toSet()));
        boolean hasNewEntries = false;

        for (Calendar date : DateHandler.fromTo(start, end)) {
            for (Map.Entry<Calendar, Set<Event>> entry : map.entrySet()) {
                if (DateUtils.isSameDay(date, entry.getKey())) {
                    eventsMap.put(date, entry.getValue());
                    hasNewEntries = true;
                }
            }
        }
        return hasNewEntries;
    }

    void submitRunnable(Runnable runnable) {
        service.submit(runnable);
    }

    void iterateEventsMap(BiConsumer<Calendar, Set<Event>> biConsumer) {
        eventsMap.forEach(biConsumer);
    }
}
