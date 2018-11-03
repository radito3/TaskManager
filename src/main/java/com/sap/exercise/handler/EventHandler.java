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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
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
    private static Map<String, Set<Event>> table = new Hashtable<>();
    private static OutputPrinter printer = new OutputPrinter(OUTPUT);

    public static void onStartup() {
        service.submit(DatabaseUtilFactory::createDbClient);
        service.submit(() -> {
            int[] today = DateHandler.getToday();
            String date = DateHandler.stringifyDate(today[0], today[1], today[2]);
            Set<Event> events = getEventsInTimeFrame(date, date);
            if (!events.isEmpty()) {
                events.forEach(EventHandler::notificationHandler);
            }
        });
    }

    private static void notificationHandler(Event event) {
        Calendar timeOf = event.getTimeOf();
        Calendar now = Calendar.getInstance();
        int timeTo = ((timeOf.get(Calendar.HOUR_OF_DAY) - now.get(Calendar.HOUR_OF_DAY)) * 60)
                + timeOf.get(Calendar.MINUTE)
                - event.getReminder();
        try {
            Thread.sleep(timeTo * 6000);
            notifyByPopup(event); //default notification
        } catch (InterruptedException e) {
            printer.error(e.getMessage());
        }
    }

    public static void create(Event event) {
        Future<Serializable> futureId = service.submit(() -> CRUDOperations.create(event));
        service.submit(() -> CRUDOperations.create(new CalendarEvents((Integer) futureId.get(), event.getTimeOf())));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            service.submit(() -> {
                try {
                    CRUDOperations.create(
                            eventStream((Integer) futureId.get(), event.getToRepeat())
                                    .collect(Collectors.toList())
                    );
                } catch (InterruptedException | ExecutionException e) {
                    printer.error(e.getMessage());
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

    public static void notifyByPopup(Event event) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), event.getTitle(), "Event reminder",
                JOptionPane.PLAIN_MESSAGE);
    }

    public static void notifyByEmail(Event event) {
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
