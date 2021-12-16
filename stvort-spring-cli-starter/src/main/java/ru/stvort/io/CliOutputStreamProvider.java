package ru.stvort.io;

import java.io.PrintStream;

public interface CliOutputStreamProvider {
    PrintStream getOutputStream();
}
