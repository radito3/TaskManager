package com.sap.exercise.commands;

import com.sap.exercise.printer.OutputPrinterProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandExecutor {

    private static Map<String, Supplier<Command>> commands = new HashMap<>(7);

    static {
        commands.put("add", AddCommand::new);
        commands.put("edit", EditCommand::new);
        commands.put("exit", ExitCommand::new);
        commands.put("delete", DeleteCommand::new);
        commands.put("help", PrintHelpCommand::new);
        commands.put("agenda", PrintAgendaCommand::new);
        commands.put("cal", PrintCalendarCommand::new);
    }

    private Command current = args -> 0;
    private String[] commandArgs = new String[] {};

    public CommandExecutor(String[] input) {
        String command = input[0];
        if (!commands.containsKey(command)) {
            OutputPrinterProvider.getPrinter().println("Invalid command");
        } else {
            current = commands.get(command).get();
            commandArgs = Arrays.copyOfRange(input, 1, input.length);
        }
    }

    public int executeCommand() {
        return current.execute(commandArgs);
    }
}
