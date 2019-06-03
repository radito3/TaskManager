package com.sap.exercise.commands;

import java.io.Serializable;

public enum CommandExecutionResult implements Serializable {
    SUCCESSFUL, EXIT;

    public boolean isEligibleForRepetition() {
        return this == SUCCESSFUL;
    }
}
