package com.sap.exercise.commands.validator;

import org.apache.commons.cli.CommandLine;

public class CommonCommandValidator implements CommandValidator {

    CommandLine cmd;

    public CommonCommandValidator(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public boolean validate() {
        return cmd != null;
    }
}
