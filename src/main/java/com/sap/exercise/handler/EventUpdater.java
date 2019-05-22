package com.sap.exercise.handler;

import com.sap.exercise.persistence.DatabaseUtilFactory;
import com.sap.exercise.handler.observers.UpdateObserver;
import com.sap.exercise.model.Event;

public class EventUpdater extends AbstractEventsHandler<Event> {

    public EventUpdater() {
        super(new UpdateObserver());
    }

    @Override
    public void execute(Event event) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> DatabaseUtilFactory.getDatabaseUtil()
                        .createTransactionBuilder()
                        .addOperation(s -> s.update(event))
                        .build()
                );
        setChanged();
        notifyObservers(event);
    }
}
