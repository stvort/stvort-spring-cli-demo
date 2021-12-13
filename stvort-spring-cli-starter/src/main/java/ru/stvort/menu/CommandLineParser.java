package ru.stvort.menu;

import java.util.List;

public interface CommandLineParser {
    List<String> parse(String commandLine);
}
