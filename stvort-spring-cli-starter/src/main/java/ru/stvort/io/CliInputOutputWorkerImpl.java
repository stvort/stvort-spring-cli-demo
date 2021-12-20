package ru.stvort.io;

import java.io.InputStream;
import java.util.Scanner;


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

    @Override
    public void printlnString(String s) {
        outputStreamProvider.getOutputStream().println(s);
    }


    @Override
    public void printString(String s) {
        outputStreamProvider.getOutputStream().print(s);
    }

    @Override
    public String inputString() {
        if (previousInputStream != inputStreamProvider.getInputStream()) {
            previousInputStream = inputStreamProvider.getInputStream();
            out = new Scanner(inputStreamProvider.getInputStream());
        }
        return out.nextLine();
    }
}
