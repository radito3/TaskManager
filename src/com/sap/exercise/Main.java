package com.sap.exercise;

import com.sap.exercise.model.Task;

public class Main {

    public static void main(String[] args) {

        Task task1 = new Task();

        Task task2 = new Task("test");

        System.out.println(task1.getBody());

        System.out.println(task2.getBody());
    }
}
