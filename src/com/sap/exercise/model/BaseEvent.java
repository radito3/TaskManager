package com.sap.exercise.model;

public abstract class BaseEvent {

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        // input filters
        this.body = body;
    }
}
