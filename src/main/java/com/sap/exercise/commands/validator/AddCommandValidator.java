package com.sap.exercise.commands.validator;

import org.apache.commons.cli.CommandLine;

public class AddCommandValidator extends DefaultCommandValidator {

    public AddCommandValidator(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void validate() {
        super.validate();
        if (cmd.getOptions().length > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
    }
}
