package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@FunctionalInterface
public interface CommandParser {

    Command parse(String[] args);

    static CommandLine safeParseCmd(Options options, String[] args) {
        try {
            return CommandUtils.getParsedCmd(options, args);
        } catch (ParseException e) {
            OutputPrinterProvider.getPrinter().println(e.getMessage());
            return null;
        }
    }
}
