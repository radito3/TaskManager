package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.PrintHelpCommand;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class PrintHelpCommandParser extends AbstractCommandParser {

    PrintHelpCommandParser() {
        super(() -> {});
    }

    public Command parse(String[] args) throws ParseException {
        Command result = super.parse(args);
        if (result != null)
            return result;

        return new PrintHelpCommand();
    }

    @Override
    Options getOptions() {
        return new Options();
    }
}
