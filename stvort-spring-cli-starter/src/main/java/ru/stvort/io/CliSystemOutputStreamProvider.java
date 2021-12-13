package ru.stvort.io;


import java.io.PrintStream;

public class CliSystemOutputStreamProvider implements CliOutputStreamProvider {
    @Override
    public PrintStream getOutputStream() {
        return System.out;
    }
}
