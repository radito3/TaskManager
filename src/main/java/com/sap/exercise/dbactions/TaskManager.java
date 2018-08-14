package com.sap.exercise.dbactions;

import com.sap.exercise.model.BaseEvent;

public class TaskManager extends BaseEvent implements EventActions {

    @Override
    public void create() {
        // database entry creation
        System.out.println("event created");
    }

    @Override
    public void update(String input) {
        // database entry updating
        setBody(input);
        System.out.println("event updated");
    }

    @Override
    public void delete() {
        // database entry deletion
        System.out.println("event deleted");
    }
}
