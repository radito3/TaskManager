package com.sap.exercise.commands;

import com.sap.exercise.db.DatabaseClientHolder;

public class ExitCommand implements Command {

    @Override
    public int execute(String... args) {
        DatabaseClientHolder.close();
        return 1;
    }
}
