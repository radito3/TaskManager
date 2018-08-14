package com.sap.exercise.model;

public abstract class BaseEvent {

    private Integer id;
    private String title;
    private String body;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // input filters
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        // input filters
        this.body = body;
    }
}
