package com.sap.exercise.printer;

import com.sap.exercise.Application;

public class OutputPrinterProvider {

    private static OutputPrinter printer;

    public static OutputPrinter getPrinter() {
        if (printer == null)
            printer = new OutputPrinter(Application.Configuration.OUTPUT);
        return printer;
    }

    public static void close() {
        printer.close();
    }
}
