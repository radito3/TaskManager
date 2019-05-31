package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.EditEventCommand;
import com.sap.exercise.commands.helper.CommandHelper;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class EditCommandParser extends AbstractCommandParser {

    EditCommandParser(CommandHelper helper) {
        super(helper);
    }

    @Override
    public Command parse(String[] args) {
        Command result = super.parse(args);
        if (result != null)
            return result;

        return new EditEventCommand(buildEventName(args));
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
