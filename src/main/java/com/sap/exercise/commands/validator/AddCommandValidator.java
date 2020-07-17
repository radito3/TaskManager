package com.sap.exercise.commands.validator;

import org.apache.commons.cli.CommandLine;

public class AddCommandValidator implements CommandValidator {

    @Override
    public void validate(CommandLine cmd) {
        if (cmd.getOptions().length > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
    }
}
