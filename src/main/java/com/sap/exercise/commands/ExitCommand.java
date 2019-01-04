package com.sap.exercise.commands;

import com.sap.exercise.db.DatabaseUtilFactory;

public class ExitCommand implements Command {

    @Override
    public int execute(String... args) {
        System.clearProperty("db-instance");
        DatabaseUtilFactory.close();
        return 1;
    }
}
