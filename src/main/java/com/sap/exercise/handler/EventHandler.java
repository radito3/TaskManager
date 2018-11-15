package com.sap.exercise.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sap.exercise.Application;
import org.apache.commons.lang3.time.DateUtils;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

//Dido : I guess this would be the controller in MVC
//Dido: I challenge you - remove the static keyword from this class! (any form of Signelton counts as static). = Will do
public class EventHandler {

    private static final ExecutorService service = Executors.newCachedThreadPool();

    // Dido: what does 'table' mean? What information does it contain, what purpose does it serve? Is this the actual model containing all
    // the data? Is it a cache of the same data, which is otherwise persisted in DB?
    private static final Map<Calendar, Set<Event>> table = new Hashtable<>();

    private static final OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    private static final EventActions actions = new EventActions();

    public static void onStartup() {
        service.submit(DatabaseUtilFactory::createDbClient);
        actions.addObserver(new CreationObserver(table, printer));
        actions.addObserver(new UpdateObserver(table));
        actions.addObserver(new DeletionObserver(table, service));
        actions.addObserver(new DeletionTimeFrameObserver(table, service));
        checkForUpcomingEvents();
    }

    // Dido: So you check for upcomming events, only when events are created/updated and when the app starts?
    // If i start the app in 23:55 at the evening, will I get tomorrow's events printed in 5-6 minutes? = Yes
    private static void checkForUpcomingEvents() {
        service.submit(() -> {
            int[] today = DateHandler.getToday();
            String date = DateHandler.stringifyDate(today[2], today[1], today[0]);
            Set<Event> events = getEventsInTimeFrame(date, date);
            if (!events.isEmpty()) {
                events.forEach(event -> service.submit(() -> Notifications.newNotificationHandler(event).run()));
            }
        });
    }

    public static void create(Event event) {
        Future<Serializable> futureId = service.submit(() -> CRUDOperations.create(event));
        service.submit(() -> CRUDOperations.create(new CalendarEvents((Integer) futureId.get(), event.getTimeOf())));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            service.submit(() -> {
                try {
                    CRUDOperations.create(eventsList((Integer) futureId.get(), event));
                } catch (InterruptedException | ExecutionException e) {
                    printer.error(e.getMessage());
                }
            });
        }

        actions.onCreate(event, futureId);
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
        Supplier<Calendar> calSupplier = () -> {
            DateHandler date = new DateHandler(event.getTimeOf());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date.asCalendar().getTime());
            return cal;
        };
        return IntStream.rangeClosed(1, endInclusive)
                .mapToObj(i -> {
                    Calendar calendar = calSupplier.get();
                    calendar.add(field, i);
                    return new CalendarEvents(eventId, calendar);
                })
                .collect(Collectors.toList());
    }

    public static void update(Event event) {
        service.submit(() -> CRUDOperations.update(event));
        actions.onUpdate(event);
        checkForUpcomingEvents();
    }

    public static void delete(Event event) {
        service.submit(() -> CRUDOperations.delete(event));
        actions.onDelete(event);
    }

    public static void deleteInTimeFrame(Event event, String start, String end) {
        service.submit(() -> CRUDOperations.deleteEventsInTimeFrame(event, start, end));
        actions.onDeleteTimeFrame(event, start, end);
    }

    public static Event getEventByTitle(String title) {
        return CRUDOperations.getEventByTitle(title)
                .orElseThrow(() -> new NullPointerException("Invalid event name"));
    }

    public static Set<Event> getEventsInTimeFrame(String start, String end) {
        Set<Event> events = new HashSet<>();
        List<String> nullDates = new ArrayList<>();

        DateHandler.fromTo(start, end)
                .forEach(handleDates(events, date -> nullDates.add(new DateHandler(date).asString())));

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

    private static Consumer<Calendar> handleDates(Set<Event> events, Consumer<Calendar> listConsumer) {
        return (date) -> {
            Set<Event> ev;
            if ((ev = table.get(date)) == null) {
                listConsumer.accept(date);
            } else {
                events.addAll(ev);
            }
        };
    }

    private static boolean setEventsInTable(String start, String end) {
        Map<Calendar, Set<Event>> map = CRUDOperations.getEventsInTimeFrame(start, end).stream()
                .collect(Collectors.groupingBy(Event::getTimeOf, Collectors.toSet()));
        boolean hasNewEntries = false;

        for (Calendar date : DateHandler.fromTo(start, end)) {
            for (Map.Entry<Calendar, Set<Event>> entry : map.entrySet()) {
                if (DateUtils.isSameDay(date, entry.getKey())) {
                    table.put(date, entry.getValue());
                    hasNewEntries = true;
                }
            }
        }
        return hasNewEntries;
    }
}
