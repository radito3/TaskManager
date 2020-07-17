package com.sap.exercise.commands.validator;

import org.apache.commons.cli.CommandLine;

public interface CommandValidator {

    void validate(CommandLine cmd);
}
