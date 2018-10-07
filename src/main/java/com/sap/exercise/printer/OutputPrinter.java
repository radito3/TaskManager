package com.sap.exercise.printer;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class OutputPrinter {

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    public static final String CURSOR_UP = "\u001B[1000A";
    public static final String CURSOR_DOWN = "\u001B[1000B";
    public static final String CURSOR_RIGHT = "\u001B[1000C";
    public static final String CURSOR_LEFT = "\u001B[1000D";

    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static final String BLACK_BRIGHT = "\u001B[90m";
    public static final String RED_BRIGHT = "\u001B[91m";
    public static final String GREEN_BRIGHT = "\u001B[92m";
    public static final String YELLOW_BRIGHT = "\u001B[93m";
    public static final String BLUE_BRIGHT = "\u001B[94m";
    public static final String PURPLE_BRIGHT = "\u001B[95m";
    public static final String CYAN_BRIGHT = "\u001B[96m";
    public static final String WHITE_BRIGHT = "\u001B[97m";

    public static final String BLACK_BACKGROUND_BRIGHT = "\u001B[100m";
    public static final String RED_BACKGROUND_BRIGHT = "\u001B[101m";
    public static final String GREEN_BACKGROUND_BRIGHT = "\u001B[102m";
    public static final String YELLOW_BACKGROUND_BRIGHT = "\u001B[103m";
    public static final String BLUE_BACKGROUND_BRIGHT = "\u001B[104m";
    public static final String PURPLE_BACKGROUND_BRIGHT = "\u001B[105m";
    public static final String CYAN_BACKGROUND_BRIGHT = "\u001B[106m";
    public static final String WHITE_BACKGROUND_BRIGHT = "\u001B[107m";

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

    public void moveCursorRight() {
        writer.print(CURSOR_RIGHT);
    }

    public void error(String val) {
        writer.println(RED + val + RESET);
    }

    public void monthCalendar(int arg) {
        this.monthCalendar(arg, false);
    }

    public void monthCalendar(int arg, boolean withEvents) {
        int month = arg > 11 ? (arg - 12) + 1 : arg + 1;
        int year = arg > 11 ? calendar.get(Calendar.YEAR) + 1 : calendar.get(Calendar.YEAR);

        String[] months = {
                "",
                "January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December"
        };

        int[] days = {
                0, 31, isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };

        writer.println("   " + months[month] + " " + year);
        writer.println(" S  M Tu  W Th  F  S");

        int startingDay = day(month, year);

        for (int i = 0; i < startingDay; i++)
            writer.print("   ");

        for (int i = 1; i <= days[month]; i++) {
            if (isToday(i, month, year))
                writer.printf(WHITE_BACKGROUND + BLACK + "%2d " + RESET, i);
            else
                printWithEvents(i, withEvents);

            if (((i + startingDay) % 7 == 0) || (i == days[month])) writer.println();
        }
    }

    private int day(int month, int year) {
        int y = year - (14 - month) / 12;
        int x = y + y / 4 - y / 100 + y / 400;
        int m = month + 12 * ((14 - month) / 12) - 2;
        return (1 + x + (31 * m) / 12) % 7;
    }

    private boolean isLeapYear(int year) {
        if  ((year % 4 == 0) && (year % 100 != 0)) return true;
        return year % 400 == 0;
    }

    private boolean isToday(int day, int month, int year) {
        return calendar.get(Calendar.DAY_OF_MONTH) == day &&
                calendar.get(Calendar.MONTH) + 1 == month &&
                calendar.get(Calendar.YEAR) == year;
    }

    private void printWithEvents(int day, boolean events) {
        if (events)
            writer.print("Not implemented");
        else
            writer.printf("%2d ", day);
    }

}
