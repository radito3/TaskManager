package com.sap.exercise.printer;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class OutputPrinter {

    private Calendar calendar = Calendar.getInstance();

    private PrintStream writer;

    //TODO create calendar output formatter and printer

    public OutputPrinter(OutputStream out) {
        writer = new PrintStream(out);
    }

    public void println(String val) {
        writer.println(val);
    }

    public void print(String val) {
        writer.print(val);
    }

}
