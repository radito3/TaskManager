package com.sap.exercise.notifications;

public interface Notification extends Runnable {

    void send();

    void setCallingThread();

    void delete();
}
