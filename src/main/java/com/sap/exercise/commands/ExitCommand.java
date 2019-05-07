package com.sap.exercise.commands;

import com.sap.exercise.db.DatabaseUtilFactory;

public class ExitCommand implements Command {

    @Override
    public int execute(String... args) {
        DatabaseUtilFactory.close();
        return 1;
    }
}
