package com.sap.exercise.commands;

import com.sap.exercise.commands.util.CommandUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.NotImplementedException;

public class Calendar implements Command {

    private java.util.Calendar cal = java.util.Calendar.getInstance();

    @Override
    public String getName() {
        return "cal";
    }

    @Override
    public void execute(String... args) {
        try {
            CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.calendarOptions(), args);

            if (cmd.hasOption('e')) {
                throw new NotImplementedException("Functionality not implemented");
            }

            if (cmd.getOptions().length > 2 || CommandUtils.optionsSizeWithoutEvents(cmd) > 1) {
                throw new IllegalArgumentException("Invalid number of arguments");
            }

            if (cmd.hasOption('3')) {
                for (int i = -1; i < 2; i++) {
                    printer.monthCalendar(cal.get(java.util.Calendar.MONTH) + i);
                    printer.println();
                }
            } else if (cmd.hasOption('y')) {
                printer.yearCalendar(cmd.getOptionValues('y') == null ? cal.get(java.util.Calendar.YEAR) : Integer.valueOf(cmd.getOptionValue('y')));
            } else {
                printer.monthCalendar(cal.get(java.util.Calendar.MONTH));
            }
        } catch (ParseException | IllegalArgumentException | NotImplementedException e) {
            printer.println(e.getMessage());
        }
    }
}
