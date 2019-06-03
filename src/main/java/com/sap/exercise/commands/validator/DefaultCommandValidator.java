package com.sap.exercise.commands.validator;

import org.apache.commons.cli.CommandLine;

import java.util.Objects;

public class DefaultCommandValidator implements CommandValidator {

    CommandLine cmd;

    public DefaultCommandValidator(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public void validate() {
        Objects.requireNonNull(cmd);
    }
}
