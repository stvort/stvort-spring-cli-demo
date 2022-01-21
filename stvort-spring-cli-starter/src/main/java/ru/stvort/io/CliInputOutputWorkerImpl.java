package ru.stvort.io;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Default implementation of interface {@link ru.stvort.io.CliInputOutputWorker}
 * based on two IO stream providers that's injected via constructor
 *
 * @author Alxander Orudzhev
 *
 * @see ru.stvort.io.CliInputOutputWorker
 * @see ru.stvort.io.CliInputStreamProvider
 * @see ru.stvort.io.CliOutputStreamProvider
 */
public class CliInputOutputWorkerImpl implements CliInputOutputWorker {

    private final CliInputStreamProvider inputStreamProvider;
    private final CliOutputStreamProvider outputStreamProvider;
    private InputStream previousInputStream;
    private Scanner out;

    public CliInputOutputWorkerImpl(CliInputStreamProvider inputStreamProvider,
                                    CliOutputStreamProvider outputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
        this.outputStreamProvider = outputStreamProvider;
    }

    /**
     * Output given string and terminate the line,
     * to given by provider OutputStream
     * @param s - string to output
     */
    @Override
    public void printlnString(String s) {
        outputStreamProvider.getOutputStream().println(s);
    }

    /**
     * Output given string without line termination,
     * to given by provider OutputStream
     * @param s - string to output
     */
    @Override
    public void printString(String s) {
        outputStreamProvider.getOutputStream().print(s);
    }

    /**
     * Reads a line of text from given by provider InputStream
     * @return the read string
     */
    @Override
    public String inputString() {
        if (previousInputStream != inputStreamProvider.getInputStream()) {
            previousInputStream = inputStreamProvider.getInputStream();
            out = new Scanner(inputStreamProvider.getInputStream());
        }
        return out.nextLine();
    }
}
