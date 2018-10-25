package com.sap.exercise.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventHandler {

    private ExecutorService service = Executors.newCachedThreadPool();

    //Threads for checking events for validity in time frame
    //Threads for reminding user (via email or pop-up) for events
    //Threads for inserting entries in DB for event creation (and deletion/updating)
    //Threshold for thread work - 30 entries
}
