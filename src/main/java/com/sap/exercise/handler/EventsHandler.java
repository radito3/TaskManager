package com.sap.exercise.handler;

public interface EventsHandler<T> {

    interface ActionType<E> {
        E getType();
    }

    void execute(T var);

    ActionType<?> getActionType();
}
