package ru.stvort.menu;

import java.util.List;

/**
 * Interface for classes that parse the command line into a list of arguments
 *
 * @author Alxander Orudzhev
 */
public interface CommandLineParser {
    /**
     * Parse the command line into a list of arguments
     * @param commandLine - command line to parse
     * @return list of arguments where command alias is the first element
     */
    List<String> parse(String commandLine);
}
