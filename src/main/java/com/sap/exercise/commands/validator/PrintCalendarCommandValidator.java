package com.sap.exercise.commands.validator;

import com.sap.exercise.commands.parser.PrintCalendarCommandParser;
import com.sap.exercise.util.ExceptionMessageHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.ArrayUtils;

public class PrintCalendarCommandValidator extends CommonCommandValidator {

    public PrintCalendarCommandValidator(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public boolean validate() {
        boolean commonCondition = super.validate();
        if (commonCondition && (cmd.getOptions().length > 2 || optionsSizeWithoutEvents() > 1)) {
            ExceptionMessageHandler.setMessage("Invalid number of arguments");
            return false;
        }
        return commonCondition;
    }

    private int optionsSizeWithoutEvents() {
        return ArrayUtils.removeElement(cmd.getOptions(),
                PrintCalendarCommandParser.getOptions().getOption("e")).length;
    }
}
