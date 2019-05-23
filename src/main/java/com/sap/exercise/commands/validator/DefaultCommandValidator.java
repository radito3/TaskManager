package com.sap.exercise.commands.validator;

import org.apache.commons.cli.CommandLine;

public class DefaultCommandValidator implements CommandValidator {

    CommandLine cmd;

    public DefaultCommandValidator(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public boolean isValid() {
        return cmd != null;
    }
}
