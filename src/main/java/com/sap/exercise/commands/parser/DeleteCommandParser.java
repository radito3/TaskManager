package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.DeleteEventCommand;
import com.sap.exercise.commands.helper.DeleteCommandHelper;
import com.sap.exercise.util.DateParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.time.LocalDate;

public class DeleteCommandParser extends AbstractCommandParser {

    DeleteCommandParser() {
        super(new DeleteCommandHelper());
    }

    @Override
    Command parseInternal(CommandLine cmd) {
        LocalDate today = LocalDate.now();
        boolean hasStartTime = false;
        boolean hasEndTime = false;
        String startTime = today.toString();
        String endTime = today.plusWeeks(1).toString();
        String eventName = buildEventName(cmd.getArgs());

        if (cmd.hasOption('s')) {
            String input = cmd.getOptionValue('s');
            startTime = new DateParser(input).asString();
            hasStartTime = true;
        }

        if (cmd.hasOption('e')) {
            String input = cmd.getOptionValue('e');
            endTime = new DateParser(input).asString();
            hasEndTime = true;
        }

        return new DeleteEventCommand(startTime, endTime, eventName, !hasStartTime && !hasEndTime);
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
                .desc("Specify the start time from when to delete entries")
                .build();
        Option end = Option.builder("e")
                .required(false)
                .longOpt("end")
                .hasArg(true)
                .numberOfArgs(1)
                .argName("date")
                .optionalArg(false)
                .desc("Specify the end time to when to delete entries")
                .build();
        Option help = Option.builder()
                .longOpt("help")
                .required(false)
                .desc("Print command help")
                .build();
        return buildOptions(start, end, help);
    }
}
