package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtil;
import com.sap.exercise.db.DatabaseUtilFactory;
import com.sap.exercise.handler.observers.CreationObserver;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EventCreator extends AbstractEventsHandler<Event> implements EventsHandler<Event> {

    public EventCreator() {
        super(new CreationObserver());
    }

    @Override
    public void execute(Event event) {
        DatabaseUtil db = DatabaseUtilFactory.getDb();
        AtomicInteger id = new AtomicInteger();
        db.beginTransaction()
                .addOperation(s -> id.set((Integer) s.save(event)))
                .addOperation(s -> s.save(new CalendarEvents(id.get(), event.getTimeOf())))
                .commit();

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            SharedResourcesFactory.getService().execute(() -> {
                DatabaseUtil.TransactionBuilder transaction = db.beginTransaction();
                eventsList(id.get(), event)
                    .forEach(calEvents -> transaction.addOperation(s -> s.save(calEvents)));
                transaction.commit();
            });
        }

        setChanged();
        //it makes more sense for the observers to be notified asynchronously.
        notifyObservers(new Object[] { event, id.get() });
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
