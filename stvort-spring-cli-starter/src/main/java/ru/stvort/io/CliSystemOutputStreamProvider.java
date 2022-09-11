package ru.stvort.io;


import java.io.PrintStream;

/**
 * Default PrintStream provider implementation for {@link ru.stvort.io.CliInputOutputWorkerImpl}
 *
 * @author Alxander Orudzhev
 *
 * @see ru.stvort.io.CliInputOutputWorkerImpl
 */
public class CliSystemOutputStreamProvider implements CliOutputStreamProvider {
    @Override
    public PrintStream getOutputStream() {
        return System.out;
    }
}
