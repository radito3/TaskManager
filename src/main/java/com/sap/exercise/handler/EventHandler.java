package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sap.exercise.Application.Configuration.OUTPUT;

public class EventHandler {

    private static final transient ExecutorService service = Executors.newCachedThreadPool();

    private static final transient Map<Calendar, Set<Event>> table = new Hashtable<>();

    private static final OutputPrinter printer = new OutputPrinter(OUTPUT);

    private static final EventActions actions = new EventActions();

    public static void onStartup() {
        service.submit(DatabaseUtilFactory::createDbClient);
        actions.addObserver(new CreationObserver(table, printer));
        actions.addObserver(new UpdateObserver(table));
        actions.addObserver(new DeletionObserver(table, service));
        actions.addObserver(new DeletionTimeFrameObserver(table, service));
        checkForUpcomingEvents();
    }

    private static void checkForUpcomingEvents() {
        service.submit(() -> {
            int[] today = DateHandler.getToday();
            String date = DateHandler.stringifyDate(today[2], today[1], today[0]);
            Set<Event> events = getEventsInTimeFrame(date, date);
            if (!events.isEmpty()) {
                events.forEach(event -> service.submit(() -> new NotificationHandler(event).run()));
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
        try {
            Future<Optional<Event>> futureEvent = service.submit(() -> CRUDOperations.getEventByTitle(title));
            return futureEvent.get()
                    .orElseThrow(() -> new NullPointerException("Invalid event name"));
        } catch (InterruptedException | ExecutionException e) {
            printer.error(e.getMessage());
        }
        return new Event();
    }

    public static Set<Event> getEventsInTimeFrame(String start, String end) {
        try {
            return service.submit(() -> {
                Set<Event> events = new HashSet<>();
                List<String> nullDates = new ArrayList<>();

                DateHandler.fromTo(start, end)
                        .forEach(handleDates(events, date -> nullDates.add(new DateHandler(date).asString())));

                if (nullDates.size() != 0) {
                    int size = table.size();
                    setEventsInTable(nullDates.get(0), nullDates.get(nullDates.size() - 1));
                    if (size != table.size()) {
                        DateHandler.fromTo(nullDates.get(0), nullDates.get(nullDates.size() - 1))
                                .forEach(handleDates(events, date -> {}));
                    }
                }

                return events;
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            printer.error(e.getMessage());
        }
        return new HashSet<>();
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

    private static void setEventsInTable(String start, String end) {
        try {
            Map<Calendar, Set<Event>> map = service.submit(() -> CRUDOperations.getEventsInTimeFrame(start, end))
                    .get().stream()
                    .collect(Collectors.groupingBy(Event::getTimeOf, Collectors.toSet()));

            for (Calendar date : DateHandler.fromTo(start, end)) {
                for (Map.Entry<Calendar, Set<Event>> entry : map.entrySet()) {
                    if (DateUtils.isSameDay(date, entry.getKey())) {
                        table.put(date, entry.getValue());
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            printer.error(e.getMessage());
        }
    }
}
