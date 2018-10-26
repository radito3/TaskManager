package com.sap.exercise.handler;

import com.sap.exercise.model.Calendar;
import com.sap.exercise.model.Event;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EventHandler {

    private ExecutorService service = Executors.newCachedThreadPool();
    private Map<Calendar, List<Event>> table = new Hashtable<>();

    public void onStartup() {
        Future<List<Event>> future = service.submit(() -> CRUDOperations.getEventsInTimeFrame("today", "today + 5"));
        try {
            List<Event> events = future.get();
            //put events in hashtable
            //when an agenda request is triggered, it first checks the table if the entries are in existing time frame
            //if not, a request to the db is executed

            List<Callable<Calendar>> calls = Arrays.asList(() -> CRUDOperations.getObject(new Calendar()),
                    () -> CRUDOperations.getObject(new Calendar()));

            service.invokeAll(calls);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Threads for checking events for validity in time frame
    //Threads for reminding user (via email or pop-up) for events
    //Threads for inserting entries in DB for event creation (and deletion/updating)
    //Threshold for thread work - 30 entries
}
