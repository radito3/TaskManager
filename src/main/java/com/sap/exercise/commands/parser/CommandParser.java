package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import org.apache.commons.cli.ParseException;

public interface CommandParser {

    Command parse(String[] args) throws ParseException;
}
