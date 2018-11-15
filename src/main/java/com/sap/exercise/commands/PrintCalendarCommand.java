package com.sap.exercise.commands;

import com.sap.exercise.commands.util.CommandUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class PrintCalendarCommand implements Command {

    private java.util.Calendar cal = java.util.Calendar.getInstance();

    @Override
    public String getName() {
        return "cal";
    }

    @Override
    public void execute(String... args) {
        try {
            CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.calendarOptions(), args);

            if (cmd.getOptions().length > 2 || CommandUtils.optionsSizeWithoutEvents(cmd) > 1) {
                throw new IllegalArgumentException("Invalid number of arguments");
            }

            if (cmd.hasOption('3')) {
                for (int i = -1; i < 2; i++) {
                    printer.monthCalendar(cal.get(java.util.Calendar.MONTH) + i, cmd.hasOption('e'));
                    printer.println();
                }
            } else if (cmd.hasOption('y')) {
                int year = cmd.getOptionValues('y') == null ? cal.get(java.util.Calendar.YEAR) : Integer.valueOf(cmd.getOptionValue('y'));
                printer.yearCalendar(year, cmd.hasOption('e'));
            } else {
                printer.monthCalendar(cal.get(java.util.Calendar.MONTH), cmd.hasOption('e'));
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
    }
}