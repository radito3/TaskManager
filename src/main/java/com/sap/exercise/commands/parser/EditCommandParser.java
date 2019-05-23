package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.EditEventCommand;
import com.sap.exercise.commands.helper.CommandHelper;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.function.Function;

public class EditCommandParser extends AbstractCommandParser {

    private Function<String[], String> eventNameBuilder;

    EditCommandParser(Function<String[], String> eventNameBuilder, CommandHelper helper) {
        super(helper);
        this.eventNameBuilder = eventNameBuilder;
    }

    @Override
    public Command parse(String[] args) {
        Command result = super.parse(args);
        if (result != null)
            return result;

        return new EditEventCommand(eventNameBuilder.apply(args));
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
