package com.sap.exercise;

import com.sap.exercise.db.DatabaseUtil;
import com.sap.exercise.model.Task;

public class Main {

    public static void main(String[] args) {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.save(new Task()));

        Task t = db.getObject(s -> s.get(Task.class, 1));
        System.out.println(t.toString());

    }

}
