package com.sap.exercise.handler;

import com.sap.exercise.listeners.UpdateListener;
import com.sap.exercise.persistence.DatabaseUtilFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.services.SharedResourcesFactory;

public class EventUpdater extends AbstractEventsHandler<Event> {

    public EventUpdater() {
        super(new UpdateListener());
    }

    @Override
    public void execute(Event event) {
        SharedResourcesFactory.getAsyncExecutionsService()
                .execute(() -> DatabaseUtilFactory.getDatabaseUtil()
                        .createTransactionBuilder()
                        .addOperation(s -> s.update(event))
                        .build()
                );
        notifyListeners(event);
    }
}
