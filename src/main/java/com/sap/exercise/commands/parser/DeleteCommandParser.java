package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.commands.DeleteCommand;
import org.apache.commons.cli.CommandLine;

public class DeleteCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(DeleteCommand.getOptions(), args)) == null)
            return () -> 0;
        String startTime = "",
                endTime = "",
                eventName = CommandUtils.buildEventName(cmd.getArgs());

        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new DeleteCommand(startTime, endTime, eventName);
    }
}
