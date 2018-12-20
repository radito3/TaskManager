package com.sap.exercise.handler;

public interface Notification extends Runnable {

    void send();

    void setCallingThread();

    void delete();
}
