package com.sap.exercise.commands.validator;

import com.sap.exercise.util.ExceptionMessageHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.ArrayUtils;

public class PrintCalendarCommandValidator extends DefaultCommandValidator {

    public PrintCalendarCommandValidator(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public boolean isValid() {
        boolean commonCondition = super.isValid();
        //TODO add filtering of 'help' flag
        if (commonCondition && (cmd.getOptions().length > 2 || optionsSizeWithoutEvents() > 1)) {
            ExceptionMessageHandler.setMessage("Invalid number of arguments");
            return false;
        }
        return commonCondition;
    }

    private int optionsSizeWithoutEvents() {
        Option withEvents = Option.builder("e")
                .required(false)
                .longOpt("events")
                .desc("Display calendar with events highlighted")
                .build();
        return ArrayUtils.removeElement(cmd.getOptions(), withEvents).length;
    }
}
