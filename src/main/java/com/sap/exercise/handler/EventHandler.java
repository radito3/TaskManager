package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sap.exercise.Application.Configuration.OUTPUT;

public class EventHandler {

    private static ExecutorService service = Executors.newCachedThreadPool();
    private static Map<String, Set<Event>> table = new Hashtable<>();
    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void onStartup() {
        service.submit(DatabaseUtilFactory::createDbClient);
        service.submit(() -> {
            int[] today = DateHandler.getToday();
            String date = DateHandler.stringifyDate(today[0], today[1], today[2]);
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
                    CRUDOperations.create(eventsList((Integer) futureId.get(), event.getToRepeat()));
                } catch (InterruptedException | ExecutionException e) {
                    printer.error(e.getMessage());
                }
            });
        }
    }

    private static List<CalendarEvents> eventsList(Integer eventId, Event.RepeatableType type) {
        Supplier<Calendar> calSupplier = Calendar::getInstance;
        switch (type) {
            case DAILY:
                return streamCreator(30, i -> eventSupplier(eventId, calSupplier.get(), Calendar.DAY_OF_MONTH, i));
            case WEEKLY:
                return streamCreator(30, i -> eventSupplier(eventId, calSupplier.get(), Calendar.WEEK_OF_YEAR, i));
            case MONTHLY:
                return streamCreator(30, i -> eventSupplier(eventId, calSupplier.get(), Calendar.MONTH, i));
            default:
                return streamCreator(4, i -> eventSupplier(eventId, calSupplier.get(), Calendar.YEAR, i));
        }
    }

    private static List<CalendarEvents> streamCreator(int endInclusive, IntFunction<CalendarEvents> func) {
        return IntStream.rangeClosed(1, endInclusive)
                .mapToObj(func)
                .collect(Collectors.toList());
    }

    private static CalendarEvents eventSupplier(Integer eventId, Calendar calendar, int field, int amount) {
        calendar.add(field, amount);
        return new CalendarEvents(eventId, calendar);
    }

    public static void update(Event event) {
        service.submit(() -> CRUDOperations.update(event));
    }

    public static void delete(Event event) {
        service.submit(() -> CRUDOperations.delete(event));
    }

    public static void deleteInTimeFrame(Event event, String start, String end) {
        service.submit(() -> CRUDOperations.deleteEventsInTimeFrame(event, start, end));
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

                DateHandler.fromTo(start, end).forEach(date -> {
                    Set<Event> ev;
                    if ((ev = table.get(date)) == null) {
                        nullDates.add(date);
                    } else {
                        events.addAll(ev);
                    }
                });

                if (nullDates.size() != 0) {
                    List<String> fromTo = DateHandler.fromTo(nullDates.get(0), nullDates.get(nullDates.size() - 1));
                    setEventsInTable(fromTo.get(0), fromTo.get(fromTo.size() - 1));
                    fromTo.forEach(date -> events.addAll(table.get(date)));
                }

                return events;
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            printer.error(e.getMessage());
        }
        return new HashSet<>();
    }

    private static void setEventsInTable(String start, String end) {
        Future<List<Event>> future = service.submit(() -> CRUDOperations.getEventsInTimeFrame(start, end));
        //TODO set them by their date
    }

    //Threads for checking events for validity in time frame
}
