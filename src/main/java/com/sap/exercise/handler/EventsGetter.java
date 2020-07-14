package com.sap.exercise.handler;

import com.sap.exercise.persistence.SessionProviderFactory;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.util.SimplifiedCalendar;
import com.sap.exercise.util.DateParser;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
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
        List<String> emptyDates = new ArrayList<>();

        for (SimplifiedCalendar date : DateParser.getRangeBetween(start, end)) {
            Set<Event> eventsInDay = SharedResourcesFactory.getEventsCache().get(date);
            if (eventsInDay == null) {
                emptyDates.add(date.toString());
            } else {
                events.addAll(eventsInDay);
            }
        }

        if (!emptyDates.isEmpty()) {
            String startIndex = emptyDates.get(0),
                    endIndex = emptyDates.get(emptyDates.size() - 1);
            if (setEventsInCache(startIndex, endIndex)) {
                for (SimplifiedCalendar date : DateParser.getRangeBetween(startIndex, endIndex)) {
                    events.addAll(SharedResourcesFactory.getEventsCache().get(date));
                }
            }
        }
        return events;
    }

    private boolean setEventsInCache(String start, String end) {
        List<CalendarEvents> calendarEvents = getCalendarEventsInRange(start, end);
        if (calendarEvents.isEmpty()) {
            return false;
        }

        Map<SimplifiedCalendar, Set<Event>> eventsPerDay = calendarEvents.stream()
                .map(calEvents -> {
                    Event event = getterFunction.apply(new Property<>("id", calEvents.getEventId()))
                            .orElseGet(Event::new);
                    event.setTimeOf(calEvents.getDate());
                    return event;
                })
                .collect(Collectors.groupingBy(calEvents -> new SimplifiedCalendar(calEvents.getTimeOf()), Collectors.toSet()));

        SharedResourcesFactory.getEventsCache().putAll(eventsPerDay);
        return true;
    }

    private List<CalendarEvents> getCalendarEventsInRange(String start, String end) {
        Session session = SessionProviderFactory.getSessionProvider().getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CalendarEvents> criteriaQuery = criteriaBuilder.createQuery(CalendarEvents.class);
        Root<CalendarEvents> root = criteriaQuery.from(CalendarEvents.class);

        criteriaQuery.where(criteriaBuilder.between(root.get("date"), start, end));
        Query<CalendarEvents> query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }
}
