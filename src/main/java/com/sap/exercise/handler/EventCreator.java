package com.sap.exercise.handler;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.db.CRUDOps;
import com.sap.exercise.handler.observers.CreationObserver;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EventCreator extends AbstractEventsHandler<Event> implements EventsHandler<Event> {

    public EventCreator() {
        super(new CreationObserver());
    }

    @Override
    public void execute(Event event) {
        //TODO naming
        CRUDOps<Event> crudOps1 = new CRUDOperations<>(Event.class);
        CRUDOps<CalendarEvents> crudOps2 = new CRUDOperations<>(CalendarEvents.class);

        //TODO - those three will be executed in individual transactions.
        //More over - if the first fails, the second one will still be attempted?
        Serializable id = crudOps1.create(event);
        crudOps2.create(new CalendarEvents((Integer) id, event.getTimeOf()));

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            //TODO Why is only this DB change executed asynchroneously? 
            SharedResourcesFactory.getService().execute(() -> crudOps2.create(eventsList((Integer) id, event)));
        }

        setChanged();
        //TODO it makes more sence for the observers to be notified asynchroneously. 
        notifyObservers(new Object[] { event, id });
    }

    private List<CalendarEvents> eventsList(Integer eventId, Event event) {
        switch (event.getToRepeat()) {
            case DAILY:
                return eventEntriesHandler(30, eventId, event, Calendar.DAY_OF_MONTH);
            case WEEKLY:
                return eventEntriesHandler(30, eventId, event, Calendar.WEEK_OF_YEAR);
            case MONTHLY:
                return eventEntriesHandler(30, eventId, event, Calendar.MONTH);
            default:
                return eventEntriesHandler(4, eventId, event, Calendar.YEAR);
        }
    }

    private List<CalendarEvents> eventEntriesHandler(int endInclusive, Integer eventId, Event event, int field) {
        return IntStream.rangeClosed(1, endInclusive)
                .mapToObj(i -> {
                    Calendar calendar = (Calendar) event.getTimeOf().clone();
                    calendar.add(field, i);
                    return new CalendarEvents(eventId, calendar);
                })
                .collect(Collectors.toList());
    }
}
