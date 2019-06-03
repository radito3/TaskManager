package com.sap.exercise.commands.validator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.ArrayUtils;

public class PrintCalendarCommandValidator extends DefaultCommandValidator {

    public PrintCalendarCommandValidator(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void validate() {
        super.validate();
        if (invalidateOptions()) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
    }

    private boolean invalidateOptions() {
        Option withEvents = Option.builder("e")
                .required(false)
                .longOpt("events")
                .desc("Display calendar with events highlighted")
                .build();
        Option help = Option.builder()
                .longOpt("help")
                .required(false)
                .desc("Print command help")
                .build();

        boolean onlyHelp = cmd.getOptions().length == 1 && cmd.getOptions()[0].equals(help);
        boolean optWithoutEvents = ArrayUtils.removeElement(cmd.getOptions(), withEvents).length > 1;
        boolean allOpts = cmd.getOptions().length > 2;

        return !onlyHelp & (optWithoutEvents | allOpts);
    }
}
