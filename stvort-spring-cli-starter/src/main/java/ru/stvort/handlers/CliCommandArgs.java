package ru.stvort.handlers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CliCommandArgs {
    private final List<String> unNamedArgs;
    private final Map<String, String> namedArgsMap;

    private CliCommandArgs(List<String> unNamedArgs, Map<String, String> namedArgsMap) {
        this.unNamedArgs = Objects.requireNonNull(unNamedArgs);
        this.namedArgsMap = Objects.requireNonNull(namedArgsMap);
    }

    public static CliCommandArgs of(List<String> source) {
        var namedArgsMap = source.stream()
                .filter(a -> a.startsWith("--"))
                .filter(a -> a.contains(":"))
                .map(a -> a.split(":", 2))
                .filter(a -> !a[0].isEmpty() && !a[1].isEmpty())
                .collect(Collectors.toMap(a -> a[0].toLowerCase().substring(2), a -> a[1]));

        var unNamedArgs = source.stream()
                .filter(a -> !a.startsWith("--"))
                .collect(Collectors.toList());
        return new CliCommandArgs(unNamedArgs, namedArgsMap);
    }

    public int getUnNamedArgsCount() {
        return unNamedArgs.size();
    }

    public String getUnNamedArgByIndex(int index) {
        Objects.checkIndex(index, unNamedArgs.size());
        return unNamedArgs.get(index);
    }

    public boolean containsNamedArg(String argName) {
        return namedArgsMap.containsKey(argName.toLowerCase());
    }

    public String getNamedArg(String argName) {
        return namedArgsMap.get(argName.toLowerCase());
    }
}
