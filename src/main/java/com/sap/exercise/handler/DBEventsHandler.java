package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseUtil;
import com.sap.exercise.model.Task;

public class DBEventsHandler /*implements EventActions<Task>*/ {
    //could be with abstract class and by reflection getting the object which is needed
    //to be modified

//    @Override
    public void create(Task obj) {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.save(obj));

        System.out.println("event created");
    }

//    @Override
    public void update(Task obj) {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.update(obj));

        System.out.println("event updated");
    }

//    @Override
    public void delete(Task obj) {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.delete(obj));

        System.out.println("event deleted");
    }
}
