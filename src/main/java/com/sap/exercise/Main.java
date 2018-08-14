package com.sap.exercise;

//import com.sap.exercise.db.Connector;
import com.sap.exercise.model.Task;

import java.util.LinkedList;
import java.util.List;

public class Main {

    private static List<Task> tasks = new LinkedList<>();

    public static void main(String[] args) {
//        Connector.test();

        Task task0 = new Task("title0", "body0");

        System.out.println(tasks.toString());

        Task task1 = new Task("title1", "body1");
        task1.update(task0);

        System.out.println(tasks.toString());

        Task task2 = new Task("title2", "body2");
        System.out.println(tasks.toString());
        task2.delete(task1);
        System.out.println(tasks.toString());
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static List<Task> getTasks() {
        return tasks;
    }
}
