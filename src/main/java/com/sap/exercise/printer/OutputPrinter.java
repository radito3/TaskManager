package com.sap.exercise.printer;

import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class OutputPrinter {

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String INVERT = "\u001B[7m";

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

    public OutputPrinter(OutputStream out) {
        writer = new PrintStream(out);
    }

    public void println(String val) {
        writer.println(val);
    }

    public void println() {
        writer.println();
    }

    public void print(String val) {
        writer.print(val);
    }

    public void monthCalendar(EventsGetterHandler handler, int month, boolean withEvents) {
        this.printCalendar(handler, month, calendar.get(Calendar.YEAR), false, withEvents);
    }

    public void yearCalendar(EventsGetterHandler handler, int year, boolean withEvents) {
        writer.println(StringUtils.leftPad(String.valueOf(year), 14));
        writer.println();
        for (int i = 0; i < 12; i++) {
            this.printCalendar(handler, i, year, true, withEvents);
            writer.println();
        }
    }

    public void printEvents(Set<Event> events) {
        Map<Event, PrinterUtils.Formatter> eventFormatters = new HashMap<>(events.size());

        PrinterUtils.mapAndSort(writer, eventFormatters, events)
                .forEach((calendar, eventList) -> {
                    Date date = calendar.getTime();
                    writer.print(YELLOW + date.toString().substring(0, 10) + RESET);

                    eventList.forEach(event -> {
                        eventFormatters.get(event).printTime(date);
                        eventFormatters.get(event).printTitle(event.getTitle());
                        writer.println();
                    });

                    writer.println();
                });
    }

    private void printCalendar(EventsGetterHandler handler, int arg, int arg1, boolean wholeYear, boolean withEvents) {
        int month = arg > 11 ? (arg - 12) + 1 : arg < 0 ? (arg + 12) + 1 : arg + 1;
        int year = arg > 11 ? arg1 + 1 : arg < 0 ? arg1 - 1 : arg1;

        Calendar cal = new GregorianCalendar(year, month - 1, 1);

        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        String monthHeader = StringUtils.leftPad(monthName,monthName.length() + (wholeYear ? 8 : 6));

        int firstWeekdayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfMonthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        writer.println(monthHeader + " " + (wholeYear ? "" : year));
        writer.println("Su  Mo  Tu  We  Th  Fr  Sa");

        int weekdayIndex = 0;

        for (int day = 1; day < firstWeekdayOfMonth; day++) {
            writer.print("    ");
            weekdayIndex++;
        }

        if (withEvents) {
            printWithEvents(handler, year, month, weekdayIndex, numberOfMonthDays);
        } else {
            for (int day = 1; day <= numberOfMonthDays; day++) {
                PrinterUtils.printDay(writer, day, month, year, "");

                weekdayIndex++;
                if (weekdayIndex == 7) {
                    weekdayIndex = 0;
                    writer.println();
                } else {
                    writer.print("  ");
                }
            }
        }
        writer.println();
    }

    private void printWithEvents(EventsGetterHandler handler, int year, int month, int weekdayIndex, int numOfMonthDays) {
        AtomicInteger weekdayInd = new AtomicInteger(weekdayIndex);

        Set<Event> events = handler.getEventsInTimeFrame(
                year + "-" + month + "-1",
                year + "-" + month + "-" + numOfMonthDays
        );

        PrinterUtils.monthEventsSorted(month, year, numOfMonthDays, events)
                .forEach(entry -> {
                    Calendar date = entry.getKey();
                    Set<Event> eventSet = entry.getValue();

                    PrinterUtils.printDay(writer, date.get(Calendar.DAY_OF_MONTH), month, date.get(Calendar.YEAR),
                            eventSet.isEmpty() ? "" : (CYAN_BACKGROUND + BLACK));

                    weekdayInd.incrementAndGet();
                    if (weekdayInd.get() == 7) {
                        weekdayInd.set(0);
                        writer.println();
                    } else {
                        writer.print("  ");
                    }
                });
    }

}
