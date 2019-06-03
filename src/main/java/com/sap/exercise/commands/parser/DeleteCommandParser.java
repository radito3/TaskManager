package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.DeleteEventCommand;
import com.sap.exercise.commands.helper.DeleteCommandHelper;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DeleteCommandParser extends AbstractCommandParser {

    DeleteCommandParser() {
        super(DeleteCommandHelper::new);
    }

    @Override
    public Command parse(String[] args) throws ParseException {
        Command result = super.parse(args);
        if (result != null)
            return result;

        String startTime = "",
                endTime = "",
                eventName = buildEventName(cmd.getArgs());

        if (cmd.hasOption('s')) {
            startTime = cmd.getOptionValue('s');
        }
        if (cmd.hasOption('e')) {
            endTime = cmd.getOptionValue('e') + "-";
        }

        return new DeleteEventCommand(startTime, endTime, eventName);
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
