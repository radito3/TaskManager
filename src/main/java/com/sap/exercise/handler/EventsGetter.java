package com.sap.exercise.handler;

import com.sap.exercise.persistence.SessionProviderFactory;
import com.sap.exercise.persistence.Property;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.util.SimplifiedCalendar;
import com.sap.exercise.util.DateHandler;
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

        for (SimplifiedCalendar date : new DateHandler(start, end).getTimeRange()) {
            Set<Event> eventsInDay = SharedResourcesFactory.getEventsMapService().getFromMap(date);
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
                for (SimplifiedCalendar date : new DateHandler(startIndex, endIndex).getTimeRange()) {
                    events.addAll(SharedResourcesFactory.getEventsMapService().getFromMap(date));
                }
            }
        }
        return events;
    }

    private boolean setEventsInCache(String start, String end) {
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

        for (SimplifiedCalendar date : new DateHandler(start, end).getTimeRange()) {
            for (Map.Entry<Calendar, Set<Event>> entry : eventsPerDay.entrySet()) {
                if (date.equals(new SimplifiedCalendar(entry.getKey()))) {
                    SharedResourcesFactory.getEventsMapService().putInMap(date, entry.getValue());
                    hasNewEntries = true;
                }
            }
        }
        return hasNewEntries;
    }
}
