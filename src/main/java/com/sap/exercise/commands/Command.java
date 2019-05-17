package com.sap.exercise.commands;

import java.io.Serializable;

public interface Command extends Serializable {

    int execute();
}