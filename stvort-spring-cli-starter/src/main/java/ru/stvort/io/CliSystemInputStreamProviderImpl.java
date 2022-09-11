package ru.stvort.io;


import java.io.InputStream;

/**
 * Default InputStream provider implementation for {@link ru.stvort.io.CliInputOutputWorkerImpl}
 *
 * @author Alxander Orudzhev
 *
 * @see ru.stvort.io.CliInputOutputWorkerImpl
 */
public class CliSystemInputStreamProviderImpl implements CliInputStreamProvider {
    @Override
    public InputStream getInputStream() {
        return System.in;
    }
}
