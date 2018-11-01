package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

import javax.swing.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sap.exercise.Main.OUTPUT;

public class EventHandler {

    private static ExecutorService service = Executors.newCachedThreadPool();
    private static Map<CalendarEvents, List<Event>> table = new Hashtable<>();
    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void onStartup() {
        service.submit(DatabaseUtilFactory::createDbClient);
        //get events for a month
        //put them in table
    }

    public static void create(Event event) {
        Future<Serializable> futureId = service.submit(() -> CRUDOperations.create(event));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            Future<Event> futureEvent = service.submit(() -> CRUDOperations.getEventById(futureId.get()));

            service.submit(() -> {
                try {
                    CRUDOperations.create(
                            eventStream(futureEvent.get().getId(), event.getToRepeat())
                                    .collect(Collectors.toList())
                    );
                } catch (InterruptedException | ExecutionException e) {
                    printer.println(e.getMessage());
                }
            });
        }
    }

    private static Stream<CalendarEvents> eventStream(Integer eventId, Event.RepeatableType type) {
        final Calendar calendar = Calendar.getInstance(); //the date is incorrectly incremented
        final IntStream stream = IntStream.range(1, 31);
        switch (type) {
            case DAILY:
                return stream
                        .mapToObj(i -> eventSupplier(eventId, calendar, Calendar.DAY_OF_MONTH, i));
            case WEEKLY:
                return stream
                        .mapToObj(i -> eventSupplier(eventId, calendar, Calendar.WEEK_OF_YEAR, i));
            case MONTHLY:
                return stream
                        .mapToObj(i -> eventSupplier(eventId, calendar, Calendar.MONTH, i));
            default:
                return IntStream.range(1, 4)
                        .mapToObj(i -> eventSupplier(eventId, calendar, Calendar.YEAR, i));
        }
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
            printer.println(e.getMessage());
        }
        return null;
    }

    public static List<Event> getEventsByDate(String date) {
        //TODO implement
        return null;
    }

    public static void notifyByPopup(Event event) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Sample message", "Sample title",
                JOptionPane.PLAIN_MESSAGE);
    }

    //Threads for checking events for validity in time frame
    //Threads for reminding user (via email or pop-up) for events
}
