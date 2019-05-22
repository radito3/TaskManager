package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.util.ExceptionMessageHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public interface CommandParser {

    Command parse(String[] args);

    static CommandLine safeParseCmd(Options options, String[] args) {
        try {
            return new DefaultParser().parse(options, args, false);
        } catch (ParseException e) {
            ExceptionMessageHandler.setMessage(e.getMessage());
            return null;
        }
    }
}
