package com.sap.exercise.handler;

import com.sap.exercise.persistence.SessionProviderFactory;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.util.DateParser;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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

        for (LocalDate date : DateParser.getRangeBetween(start, end)) {
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
                for (LocalDate date : DateParser.getRangeBetween(startIndex, endIndex)) {
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

        Map<LocalDate, Set<Event>> eventsPerDay = new HashMap<>();

        for (CalendarEvents calEvents : calendarEvents) {
            Optional<Event> optional = getterFunction.apply(new Property<>("id", calEvents.getEventId()));
            if (!optional.isPresent()) {
                continue;
            }
            Event event = optional.get();
            eventsPerDay.computeIfAbsent(event.getTimeOf().toLocalDate(), k -> new HashSet<>())
                        .add(event);
        }

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
