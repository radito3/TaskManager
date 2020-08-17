package com.sap.exercise.commands;

import com.sap.exercise.Configuration;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;
import com.sap.exercise.builder.EventBuilder;
import com.sap.exercise.builder.FieldInfo;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class EventDataParser {

    private volatile String input;
    private final EventBuilder eventBuilder;
    private final OutputPrinter printer = OutputPrinterProvider.getPrinter();

    EventDataParser(EventBuilder eventBuilder) {
        this.eventBuilder = eventBuilder;
    }

    void parseInput() {
        for (FieldInfo fieldInfo : eventBuilder.getFields()) {
            printer.print(fieldInfo.getNameToDisplay() + ": " + PrinterColors.CURSOR_RIGHT);

            readInput(fieldInfo);

            if (!input.isEmpty()) {
                fieldInfo.parseArgument(input);
            }
        }
    }

    private void readInput(FieldInfo fieldInfo) {
        boolean error = false;
        String before = input;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new CloseShieldInputStream(Configuration.INPUT)))) {

            input = reader.readLine();
            checkMandatoryField(reader, fieldInfo);

        } catch (IOException e) {
            Logger.getLogger(getClass()).error("Input reading error", e);
            printer.println("Error reading input. Please try again");
            error = true;
        } finally {
            if (error && input.equals(before)) {
                readInput(fieldInfo);
            }
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
