package com.sap.exercise.commands;

import java.io.Serializable;

public enum CommandExecutionResult implements Serializable {
    SUCCESSFUL, ERROR, EXIT;

    public boolean isSuccessful() {
        return this == SUCCESSFUL;
    }
}
