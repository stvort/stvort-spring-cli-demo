package ru.stvort.handlers;

/**
 * Interface for classes containing logic for processing
 * commands represented as a string
 *
 * @author Alxander Orudzhev
 */
public interface CliCommandsHandler {

    /**
     * Handle (execute) given command and return it result if necessary
     * @param commandLine - command line represented as a string
     * @return result of command execution or {@code null} if command does not suggest a result
     */
    Object handle(String commandLine);
}
