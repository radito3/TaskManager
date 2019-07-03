package com.sap.exercise.handler;

import com.sap.exercise.persistence.SessionProviderFactory;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.util.CalendarWrapper;
import com.sap.exercise.util.DateHandler;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

class EventsGetter {

    private final String start;
    private final String end;
    private final Function<Property<Integer>, Optional<Event>> getterFunction;

    EventsGetter(String start, String end, Function<Property<Integer>, Optional<Event>> getterFunction) {
        this.start = start;
        this.end = end;
        this.getterFunction = getterFunction;
    }

    Set<Event> getEventsInTimeFrame() {
        Set<Event> events = new HashSet<>();
        List<String> nullDates = new ArrayList<>(5);

        new DateHandler(start, end).fromTo()
                .forEach(handleDates(events, date -> nullDates.add(date.toString())));

        if (!nullDates.isEmpty()) {
            String startIndex = nullDates.get(0),
                    endIndex = nullDates.get(nullDates.size() - 1);
            if (setEventsInTable(startIndex, endIndex)) {
                new DateHandler(startIndex, endIndex).fromTo()
                        .forEach(handleDates(events, date -> {}));
            }
        }
        return events;
    }

    private Consumer<CalendarWrapper> handleDates(Set<Event> events, Consumer<CalendarWrapper> consumer) {
        return (CalendarWrapper date) -> {
            Set<Event> ev;
            if ((ev = SharedResourcesFactory.getEventsMapService().getFromMap(date)) == null) {
                consumer.accept(date);
            } else {
                events.addAll(ev);
            }
        };
    }

    private boolean setEventsInTable(String start, String end) {
        boolean hasNewEntries = false;

        Session session = SessionProviderFactory.getSessionProvider().getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CalendarEvents> criteriaQuery = criteriaBuilder.createQuery(CalendarEvents.class);
        Root<CalendarEvents> root = criteriaQuery.from(CalendarEvents.class);

        criteriaQuery.where(criteriaBuilder.between(root.get("date"), start, end));
        Query<CalendarEvents> query = session.createQuery(criteriaQuery);

        Map<Calendar, Set<Event>> eventsPerDay = query.getResultList().stream()
                .map(calEvents -> {
                    Event event = getterFunction.apply(new Property<>("id", calEvents.getEventId()))
                            .orElseGet(Event::new);
                    event.setTimeOf(calEvents.getDate());
                    return event;
                })
                .collect(Collectors.groupingBy(Event::getTimeOf, Collectors.toSet()));

        for (CalendarWrapper date : new DateHandler(start, end).fromTo()) {
            for (Map.Entry<Calendar, Set<Event>> entry : eventsPerDay.entrySet()) {
                if (date.equals(new CalendarWrapper(entry.getKey()))) {
                    SharedResourcesFactory.getEventsMapService().putInMap(date, entry.getValue());
                    hasNewEntries = true;
                }
            }
        }
        return hasNewEntries;
    }
}
