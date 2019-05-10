package com.sap.exercise.commands;

import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;
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
    private final OutputPrinter printer = OutputPrinterProvider.getPrinter();

    InteractiveInput(BufferedReader reader, EventWrapper wrapper) {
        this.reader = reader;
        this.eventWrapper = wrapper;
    }

    void parseInput() {
        for (FieldInfo fieldInfo : eventWrapper.getFields()) {
            printer.print(fieldInfo.getNameToDisplay() + ": " + PrinterColors.CURSOR_RIGHT);

            readInput(fieldInfo);

            if (!input.isEmpty())
                fieldInfo.handleArg(input);
        }
    }

    private void readInput(FieldInfo fieldInfo) {
        boolean error = false;
        try {
            input = reader.readLine();
            checkMandatoryField(reader, fieldInfo);
        } catch (IOException e) {
            Logger.getLogger(getClass()).error("Input reading error", e);
            printer.println("Error on read. Please try again");
            error = true;
        } finally {
            if (error)
                readInput(fieldInfo);
        }
    }

    private void checkMandatoryField(BufferedReader reader, FieldInfo fieldInfo) throws IOException {
        if (fieldInfo.isMandatory() && input.isEmpty()) {
            do {
                printer.println("Field is mandatory!");
                printer.print(fieldInfo.getNameToDisplay() + ": " + PrinterColors.CURSOR_RIGHT);

                input = reader.readLine();
            } while (input.isEmpty());
        }
    }
}
