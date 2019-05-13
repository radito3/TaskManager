package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.PrintAgendaCommand;
import org.apache.commons.cli.CommandLine;

public class PrintAgendaCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(PrintAgendaCommand.getOptions(), args)) == null)
            return () -> 0;
        String startTime = "", endTime = "";

        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new PrintAgendaCommand(startTime, endTime);
    }
}
