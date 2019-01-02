package com.sap.exercise.handler;

import com.sap.exercise.db.CRUDOperations;
import com.sap.exercise.handler.observers.UpdateObserver;
import com.sap.exercise.model.Event;

public class EventUpdater extends AbstractEventsHandler<Event> implements EventsHandler<Event> {

    public enum UpdateType implements ActionType<UpdateType> {
        UPDATE;

        @Override
        public UpdateType getType() {
            return UPDATE;
        }
    }

    public EventUpdater() {
        super(new UpdateObserver());
    }

    @Override
    public void execute(Event event) {
        thPool.submit(() -> new CRUDOperations<>(Event.class).update(event));
        setChanged();
        notifyObservers(new Object[] { event });
    }

    @Override
    public ActionType<?> getActionType() {
        return UpdateType.UPDATE.getType();
    }
}
