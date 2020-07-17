package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.PrintAgendaCommand;
import com.sap.exercise.commands.helper.PrintAgendaCommandHelper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.time.LocalDate;

public class PrintAgendaCommandParser extends AbstractCommandParser {

    PrintAgendaCommandParser() {
        super(new PrintAgendaCommandHelper());
    }

    @Override
    Command parseInternal(CommandLine cmd) {
        String today = LocalDate.now().toString();
        String startTime = today,
            endTime = today;

        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new PrintAgendaCommand(startTime, endTime);
    }

    @Override
    public Options getOptions() {
        Option start = Option.builder("s")
                .required(false)
                .longOpt("start")
                .hasArg(true)
                .numberOfArgs(1)
                .argName("date")
                .optionalArg(false)
                .desc("Specify the start time from when to get entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .argName("date")
                .optionalArg(false)
                .desc("Specify the end time to when to get entries")
                .build();
        Option help = Option.builder()
                .longOpt("help")
                .required(false)
                .desc("Print command help")
                .build();
        return buildOptions(start, end, help);
    }
}
