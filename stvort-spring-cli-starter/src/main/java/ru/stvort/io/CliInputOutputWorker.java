package ru.stvort.io;

/**
 * Common interface for IO operations, that used by {@link ru.stvort.menu.MenuLoop}
 *
 * @author Alexander Orudzhev
 * @see ru.stvort.menu.MenuLoop
 */
public interface CliInputOutputWorker {

    /**
     * Output given string and terminate the line
     * @param s - string to output
     */
    void printlnString(String s);

    /**
     * Output given string without line termination
     * @param s - string to output
     */
    void printString(String s);

    /**
     * Reads a line of text
     * @return the read string
     */
    String inputString();
}
