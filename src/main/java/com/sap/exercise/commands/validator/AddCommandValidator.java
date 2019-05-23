package com.sap.exercise.commands.validator;

import com.sap.exercise.util.ExceptionMessageHandler;
import org.apache.commons.cli.CommandLine;

public class AddCommandValidator extends DefaultCommandValidator {

    public AddCommandValidator(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public boolean isValid() {
        boolean commonCondition = super.isValid();
        if (commonCondition && cmd.getOptions().length > 1) {
            ExceptionMessageHandler.setMessage("Invalid number of arguments");
            return false;
        }
        return commonCondition;
    }
}
