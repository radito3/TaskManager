package com.sap.exercise.commands;

import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.printer.PrinterColors;

public class PrintHelpCommand implements Command {

    @Override
    public CommandExecutionResult execute() {
        OutputPrinterProvider.getPrinter().printf(
                "TaskManager version 1.0%n" +
                "Usage: command [options...]%n%n" +
                PrinterColors.BOLD + "Available commands:%n" + PrinterColors.RESET +
                " add     Add an event%n" +
                " edit    Edit an event%n" +
                " delete  Delete event entries (all/in time frame)%n" +
                " agenda  Show (weekly) agenda%n" +
                " cal     Show calendar");

        return CommandExecutionResult.SUCCESSFUL;
    }
}
