package com.sap.exercise.model;

import com.sap.exercise.dbactions.TaskManager;

public class Task extends TaskManager {

    public Task() {
        setBody("task body");
    }

    public Task(String text) {
        setBody(text);
    }


}
