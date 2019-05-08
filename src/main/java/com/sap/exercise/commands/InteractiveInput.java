package com.sap.exercise.commands;

import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.wrapper.EventWrapper;
import com.sap.exercise.wrapper.FieldInfo;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

// The name of this class is not very meaningful. As far as I understand, it is supposed to further request Event specific data from the user
// InteractiveInput does not hint at its true meaning at first sight
class InteractiveInput {

    private String input;
    private final BufferedReader reader;
    private final EventWrapper eventWrapper;

    InteractiveInput(BufferedReader reader, EventWrapper wrapper) {
        this.reader = reader;
        this.eventWrapper = wrapper;
    }

    void parseInput() {
        try {
            for (FieldInfo fieldInfo : eventWrapper.getFields()) {
                Command.printer.print(fieldInfo.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                input = reader.readLine();
                checkMandatoryField(reader, fieldInfo);

                if (!input.isEmpty()) {
                    fieldInfo.handleArg(input);
                }
            }
        } catch (IOException e) { // The exception is not propagated through callers
                                  // What would happen if readLine (line 29) fails, throwing an IOException, and the field is mandatory. 
                                  // It would not set a field which is mandatory, but neither will throw an exception further up the chain, 
                                  // which would possibly lead to execution of some command with wrong data. Consider letting this exception propagate instead of catching it, or atleast rethrow it
            Logger.getLogger(getClass()).error("Input reading error", e);
        }
    }

    private void checkMandatoryField(BufferedReader reader, FieldInfo fieldInfo) throws IOException {
        if (fieldInfo.isMandatory() && input.isEmpty()) {
            do {
                Command.printer.println("Field is mandatory!");
                Command.printer.print(fieldInfo.getNameToDisplay() + ": " + OutputPrinter.CURSOR_RIGHT);

                input = reader.readLine();
            } while (input.isEmpty());
        }
    }
}
