package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.model.Event;

import java.util.Observable;
import java.util.Observer;

public class DeletionObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        Event event = (Event) o;

        SharedResourcesFactory.getService().execute(() ->
                SharedResourcesFactory.getMapHandler().iterateEventsMap(
                        (cal, set) -> set.removeIf(event1 -> event1.getId().equals(event.getId()))
                )
        );
    }
}
