package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.EditEventCommand;
import com.sap.exercise.commands.helper.EditCommandHelper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class EditCommandParser extends AbstractCommandParser {

    EditCommandParser() {
        super(opts -> new EditCommandHelper());
    }

    @Override
    Command parseInternal(CommandLine cmd) {
        return new EditEventCommand(buildEventName(cmd.getArgs()));
    }

    @Override
    public Options getOptions() {
        Option help = Option.builder()
                .longOpt("help")
                .required(false)
                .desc("Print command help")
                .build();
        return buildOptions(help);
    }
}
