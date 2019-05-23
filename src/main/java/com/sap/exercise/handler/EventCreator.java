package com.sap.exercise.handler;

import com.sap.exercise.listeners.CreationListener;
import com.sap.exercise.persistence.TransactionBuilder;
import com.sap.exercise.persistence.TransactionBuilderFactory;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EventCreator extends AbstractEventsHandler<Event> {

    public EventCreator() {
        super(new CreationListener());
    }

    @Override
    public void execute(Event event) {
        AtomicInteger id = new AtomicInteger();
        TransactionBuilderFactory.getTransactionBuilder()
                .addOperation(s -> id.set((Integer) s.save(event)))
                .addOperation(s -> s.save(new CalendarEvents(id.get(), event.getTimeOf())))
                .commit();

        if (event.getToRepeat() != Event.RepeatableType.NONE) {
            SharedResourcesFactory.getAsyncExecutionsService().execute(() -> {
                TransactionBuilder transaction = TransactionBuilderFactory.getTransactionBuilder();
                eventsList(id.get(), event)
                    .forEach(calEvents -> transaction.addOperation(s -> s.save(calEvents)));
                transaction.commit();
            });
        }

        notifyListeners(new Object[] { event, id.get() });
    }

    private List<CalendarEvents> eventsList(Integer eventId, Event event) {
        switch (event.getToRepeat()) {
            case DAILY:
                return handleEventEntries(30, eventId, event, Calendar.DAY_OF_MONTH);
            case WEEKLY:
                return handleEventEntries(30, eventId, event, Calendar.WEEK_OF_YEAR);
            case MONTHLY:
                return handleEventEntries(30, eventId, event, Calendar.MONTH);
            default:
                return handleEventEntries(4, eventId, event, Calendar.YEAR);
        }
    }

    private List<CalendarEvents> handleEventEntries(int endInclusive, Integer eventId, Event event, int field) {
        return IntStream.rangeClosed(1, endInclusive)
                .mapToObj(i -> {
                    Calendar calendar = (Calendar) event.getTimeOf().clone();
                    calendar.add(field, i);
                    return new CalendarEvents(eventId, calendar);
                })
                .collect(Collectors.toList());
    }
}
