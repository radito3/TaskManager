package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sap.exercise.Main.OUTPUT;

public class EventHandler {

    private static ExecutorService service = Executors.newCachedThreadPool();
    private static Map<String, List<Event>> table = new Hashtable<>();
    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void onStartup() {
        service.submit(DatabaseUtilFactory::createDbClient);
    }

    public static void create(Event event) {
        Future<Serializable> futureId = service.submit(() -> CRUDOperations.create(event));
        Future<Event> futureEvent = service.submit(() -> CRUDOperations.getEventById(futureId.get()));
        service.submit(() -> CRUDOperations.create(new CalendarEvents(futureEvent.get().getId(), futureEvent.get().getTimeOf())));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
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

    //for calendar
    //this should return a string representing a colour
    //so that the calendar printer can print it
    public static String getEventsByDate(String date) {
        //this needs to be async
        List<Event> events = table.get(date) == null ? CRUDOperations.getEventsInTimeFrame(date, date) : table.get(date);
        String result = "";

        Supplier<Stream<Event.EventType>> eventStreamSupplier = () -> events.stream().map(Event::getTypeOf);
        if (eventStreamSupplier.get().anyMatch(type -> type.equals(Event.EventType.TASK))) {
            result = OutputPrinter.GREEN;
        } //else for reminder and goal

        return result;
    }

    //for agenda
    public static List<Event> getEventsInTimeFrame(String start, String end) {
        List<Event> events = new ArrayList<>();
        List<String> nullDates = new ArrayList<>();
        DateHandler.fromTo(start, end).forEach(date -> {
            List<Event> ev;
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

        events.sort(Comparator.comparing(Event::getTimeOf));
        return events;
    }

    private static void setEventsInTable(String start, String end) {
        //TODO implement
    }

    public static void notifyByPopup(Event event) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Sample message", "Sample title",
                JOptionPane.PLAIN_MESSAGE);
    }

    public static void notifyByEmail(Event event) {
        Logger.getLogger("javax.mail").setLevel(Level.FINE);

        Properties props = new Properties();
        props.put("mail.smtp.host", "my-mail-server");
        Session session = Session.getInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("me@example.com");
            msg.setRecipients(Message.RecipientType.TO,
                    "you@example.com");
            msg.setSubject("JavaMail hello world example");
            msg.setSentDate(new Date());
            msg.setText("Hello, world!\n");
            Transport.send(msg, "me@example.com", "my-password");
        } catch (MessagingException mex) {
            printer.println("send failed, exception: " + mex);
        }
    }

    //Threads for checking events for validity in time frame
}
