package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.PrintCalendarCommand;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.ArrayUtils;

public class PrintCalendarCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(PrintCalendarCommand.getOptions(), args)) == null)
            return () -> 0;
        if (cmd.getOptions().length > 2 || optionsSizeWithoutEvents(cmd) > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        return new PrintCalendarCommand(cmd);
    }

    private int optionsSizeWithoutEvents(CommandLine cmd) {
        return ArrayUtils.removeElement(cmd.getOptions(), PrintCalendarCommand.getOptions().getOption("e")).length;
    }
}
