package com.sap.exercise.handler;

//TODO need to modify this to properly use it to decouple modules
public interface EventsHandler<T> {

    void execute(T var);
}
