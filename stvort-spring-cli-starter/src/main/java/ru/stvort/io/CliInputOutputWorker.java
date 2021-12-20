package ru.stvort.io;

/**
 * Common interface for IO operations
 *
 * @author Alexander Orudzhev
 */
public interface CliInputOutputWorker {
    void printlnString(String s);

    void printString(String s);

    String inputString();
}
