package com.sap.exercise.commands;

import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.FieldInfo;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

class InteractiveInput {

    private String input;
    private final BufferedReader reader;
    private final EventWrapper wrapper;

    InteractiveInput(BufferedReader reader, EventWrapper wrapper) {
        this.reader = reader;
        this.wrapper = wrapper;
    }

    void parseInput() {
        try {
            for (FieldInfo field : wrapper.getFields()) {
                Command.printer.print(field.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                input = reader.readLine();
                checkMandatoryField(reader, field);

                if (!input.isEmpty()) {
                    field.handleArg(input);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(getClass()).error("Input reading error", e);
        }
    }

    private void checkMandatoryField(BufferedReader reader, FieldInfo fInfo) throws IOException {
        if (fInfo.isMandatory() && input.isEmpty()) {
            do {
                Command.printer.println("Field is mandatory!");
                Command.printer.print(fInfo.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                input = reader.readLine();
            } while (input.isEmpty());
        }
    }
}
