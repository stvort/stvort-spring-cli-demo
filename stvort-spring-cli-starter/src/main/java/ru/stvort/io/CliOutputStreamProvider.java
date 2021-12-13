package ru.stvort.io;

import java.io.InputStream;
import java.io.PrintStream;

public interface CliOutputStreamProvider {
    PrintStream getOutputStream();
}
