package ru.stvort.io;


import java.io.InputStream;

public class CliSystemInputStreamProviderImpl implements CliInputStreamProvider {
    @Override
    public InputStream getInputStream() {
        return System.in;
    }
}
