package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.commands.PrintAgendaCommand;
import com.sap.exercise.commands.validator.CommandValidator;
import com.sap.exercise.commands.validator.CommonCommandValidator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PrintAgendaCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd = CommandParser.safeParseCmd(getOptions(), args);
        CommandValidator validator = new CommonCommandValidator(cmd);
        if (!validator.validate())
            return () -> CommandExecutionResult.ERROR;

        String startTime = "", endTime = "";

        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new PrintAgendaCommand(startTime, endTime);
    }

    public static Options getOptions() {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the start time from when to get entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .optionalArg(false)
                .desc("Specify the end time to when to get entries")
                .build();
        return CommandParserFactory.buildOptions(start, end);
    }
}
