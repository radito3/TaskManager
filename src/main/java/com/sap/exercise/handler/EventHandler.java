
package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
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
        Future<Serializable> future = service.submit(() -> CRUDOperations.create(event));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            Future<Event> eventFuture = service.submit(() -> CRUDOperations.getEventById(future.get()));

            service.submit(() -> {
                try {
                    CRUDOperations.create(createEvents(eventFuture.get().getId(), event.getToRepeat())
                        .collect(Collectors.toList()));
                } catch (InterruptedException | ExecutionException e) {
                    printer.println(e.getMessage());
                }
            });
        }
    }

    private static Stream<CalendarEvents> createEvents(Integer eventId, Event.RepeatableType type) {


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

    public static void delete(Event event) {
        //implement
    }

    //Threads for checking events for validity in time frame
    //Threads for reminding user (via email or pop-up) for events
}
