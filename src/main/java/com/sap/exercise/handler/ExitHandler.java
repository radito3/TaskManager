package com.sap.exercise.handler;

import com.sap.exercise.handler.observers.ExitObserver;
import com.sap.exercise.model.Event;

public class ExitHandler extends AbstractEventsHandler<Event> implements ExitHndl {

    public ExitHandler() {
        super(new ExitObserver());
    }

    @Override
    public void execute(Event var) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ActionType<?> getActionType() {
        return null;
    }

    @Override
    public void execute() {
        setChanged();
        notifyObservers();
    }
}
