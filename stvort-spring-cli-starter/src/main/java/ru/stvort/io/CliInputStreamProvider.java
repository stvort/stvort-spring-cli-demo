package ru.stvort.io;

import java.io.InputStream;

/**
 * Common interface of InputStream provider for {@link ru.stvort.io.CliInputOutputWorkerImpl}
 *
 * @author Alxander Orudzhev
 *
 * @see ru.stvort.io.CliInputOutputWorkerImpl
 */
public interface CliInputStreamProvider {
    InputStream getInputStream();
}
