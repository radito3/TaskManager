package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.ExitCommand;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ExitCommandParser extends AbstractCommandParser {

    ExitCommandParser() {
        super(() -> {});
    }

    public Command parse(String[] args) throws ParseException {
        Command result = super.parse(args);
        if (result != null)
            return result;

        return new ExitCommand();
    }

    @Override
    Options getOptions() {
        return new Options();
    }
}
