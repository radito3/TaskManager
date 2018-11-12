package com.sap.exercise.handler;

import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CreationObserver implements Observer {

    private Map<Calendar, Set<Event>> table;
    private OutputPrinter printer;

    CreationObserver(Map<Calendar, Set<Event>> table, OutputPrinter printer) {
        this.table = table;
        this.printer = printer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable observable, Object o) {
        Object[] objects = (Object[]) o;
        Event event = (Event) objects[0];
        EventActions.ActionType type = (EventActions.ActionType) objects[1];

        if (type == EventActions.ActionType.CREATE) {
            Future<Serializable> futureId = (Future<Serializable>) objects[2];
            int[] today = DateHandler.getToday();
            String date = DateHandler.stringifyDate(today[2], today[1], today[0]);

            table.forEach((cal, eventSet) -> {
                if (DateUtils.isSameDay(DateHandler.fromTo(date, date).get(0), cal)) {
                    try {
                        event.setId((Integer) futureId.get());
                    } catch (InterruptedException | ExecutionException e) {
                        printer.error(e.getMessage());
                    }
                    eventSet.add(event);
                }
            });
        }
    }
}
