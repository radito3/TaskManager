package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.PrintHelpCommand;
import org.apache.commons.cli.CommandLine;

public class PrintHelpCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(PrintHelpCommand.getOptions(), args)) == null)
            return () -> 0;
        if (cmd.getOptions().length > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        return new PrintHelpCommand(cmd);
    }
}
