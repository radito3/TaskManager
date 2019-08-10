package com.sap.exercise.commands.parser;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CommandParsers {
    ADD(AddCommandParser::new),
    EDIT(EditCommandParser::new),
    DELETE(DeleteCommandParser::new),
    CAL(PrintCalendarCommandParser::new),
    AGENDA(PrintAgendaCommandParser::new),
    HELP(PrintHelpCommandParser::new),
    EXIT(ExitCommandParser::new);

    private final Supplier<CommandParser> supplier;

    CommandParsers(Supplier<CommandParser> supplier) {
        this.supplier = supplier;
    }

    Supplier<CommandParser> getSupplier() {
        return supplier;
    }

    private static final Map<String, Supplier<CommandParser>> valuesMap = Stream.of(CommandParsers.values())
                .collect(Collectors.toMap(e -> e.toString().toLowerCase(), CommandParsers::getSupplier));

    public static CommandParser getParser(String command) {
        return Optional.ofNullable(valuesMap.get(command))
            .orElseThrow(() -> new IllegalArgumentException("Invalid command")) 
            .get()
    }
}
