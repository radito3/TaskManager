package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;

public interface CommandParser {
    Command parse(String[] args);
}
