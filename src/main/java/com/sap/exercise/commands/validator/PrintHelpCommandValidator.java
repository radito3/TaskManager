package com.sap.exercise.commands.validator;

import com.sap.exercise.util.ExceptionMessageHandler;
import org.apache.commons.cli.CommandLine;

public class PrintHelpCommandValidator extends CommonCommandValidator {

    public PrintHelpCommandValidator(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public boolean validate() {
        boolean commonCondition = super.validate();
        if (commonCondition && cmd.getOptions().length > 1) {
            ExceptionMessageHandler.setMessage("Invalid number of arguments");
            return false;
        }
        return commonCondition;
    }
}
