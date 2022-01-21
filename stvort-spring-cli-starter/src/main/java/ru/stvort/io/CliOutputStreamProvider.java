package ru.stvort.io;

import java.io.PrintStream;

/**
 * Common interface of PrintStream provider for {@link ru.stvort.io.CliInputOutputWorkerImpl}
 *
 * @author Alxander Orudzhev
 *
 * @see ru.stvort.io.CliInputOutputWorkerImpl
 */
public interface CliOutputStreamProvider {
    PrintStream getOutputStream();
}
