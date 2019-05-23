package com.sap.exercise.handler;

import com.sap.exercise.persistence.TransactionBuilderFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.util.CalendarWrapper;
import com.sap.exercise.util.DateHandler;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventGetter extends AbstractEventsHandler<Event> implements EventsGetterHandler {

    @Override
    @SuppressWarnings("unchecked")
    public Event getEventByTitle(String var) {
        Optional<Event> optionalEvent = (Optional<Event>) TransactionBuilderFactory.getTransactionBuilder()
                .addOperationWithResult(s -> s.createNativeQuery("SELECT * FROM Eventt WHERE Title = \'"
                        + var + "\' LIMIT 1;", Event.class).uniqueResultOptional())
                .commit()
                .iterator().next();

        return optionalEvent.orElseThrow(() -> new NoSuchElementException("Invalid event name"));
    }

    @Override
    public Set<Event> getEventsInTimeFrame(String start, String end) {
        Set<Event> events = new HashSet<>();
        List<String> nullDates = new LinkedList<>();

        new DateHandler(start, end).fromTo()
                .forEach(handleDates(events, date -> nullDates.add(date.toString())));

        if (nullDates.size() != 0) {
            String startIndex = nullDates.get(0),
                    endIndex = nullDates.get(nullDates.size() - 1);
            if (setEventsInTable(startIndex, endIndex)) {
                new DateHandler(startIndex, endIndex).fromTo()
                        .forEach(handleDates(events, date -> {}));
            }
        }
        return events;
    }

    private Consumer<CalendarWrapper> handleDates(Set<Event> events, Consumer<CalendarWrapper> listConsumer) {
        return (CalendarWrapper date) -> {
            Set<Event> ev;
            if ((ev = SharedResourcesFactory.getEventsMapService().getFromMap(date)) == null) {
                listConsumer.accept(date);
            } else {
                events.addAll(ev);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private boolean setEventsInTable(String start, String end) {
        boolean hasNewEntries = false;
        List<CalendarEvents> list = (List<CalendarEvents>) TransactionBuilderFactory.getTransactionBuilder()
                .addOperationWithResult(s ->
                        s.createNativeQuery("SELECT * FROM CalendarEvents WHERE Date >= \'" + start +
                                "\' AND Date <= \'" + end + "\';", CalendarEvents.class).getResultList())
                .commit()
                .iterator().next();

        Map<Calendar, Set<Event>> map = list.stream()
                .map(calEvents -> {
                    Event event = (Event) TransactionBuilderFactory.getTransactionBuilder()
                            .addOperationWithResult(s -> s.get(Event.class, calEvents.getEventId()))
                            .commit()
                            .iterator().next();
                    event.setTimeOf(calEvents.getDate());
                    return event;
                })
                .collect(Collectors.groupingBy(Event::getTimeOf, Collectors.toSet()));

        for (CalendarWrapper date : new DateHandler(start, end).fromTo()) {
            for (Map.Entry<Calendar, Set<Event>> entry : map.entrySet()) {
                if (date.equals(new CalendarWrapper(entry.getKey()))) {
                    SharedResourcesFactory.getEventsMapService().putInMap(date, entry.getValue());
                    hasNewEntries = true;
                }
            }
        }
        return hasNewEntries;
    }
}
