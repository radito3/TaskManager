package com.sap.exercise.handler.observers;

import com.sap.exercise.handler.AbstractEventsHandler;

import java.util.Observable;
import java.util.Observer;

public class ExitObserver implements Observer {

    @Override
    public void update(Observable observable, Object o) {
        AbstractEventsHandler<?> handler = (AbstractEventsHandler<?>) observable;
        handler.getThPool().close();
    }
}
