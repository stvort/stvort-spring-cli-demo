package ru.stvort.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс для работы с вводом-выводом должен ")
@ExtendWith(MockitoExtension.class)
class CliInputOutputWorkerImplTest {

    public static final String EXPECTED_STRING_FOR_OUTPUT = "Expected string";

    @Mock
    private CliInputStreamProvider inputStreamProvider;

    @Mock
    private CliOutputStreamProvider outputStreamProvider;

    @InjectMocks
    private CliInputOutputWorkerImpl io;

    @DisplayName("печатать строку в предоставляемый провайдером поток, добавляя в конце перевод на новую строку ")
    @Test
    void shouldPrintlnStringToGivenByProviderStream() {
        var out = new ByteArrayOutputStream();
        given(outputStreamProvider.getOutputStream()).willReturn(new PrintStream(out));

        io.printlnString(EXPECTED_STRING_FOR_OUTPUT);
        var actualString = out.toString();
        assertThat(actualString).isEqualTo(EXPECTED_STRING_FOR_OUTPUT + System.lineSeparator());
    }

    @DisplayName("печатать строку в предоставляемый провайдером поток, не добавляя в конце перевод на новую строку")
    @Test
    void shouldPrintStringToGivenByProviderStream() {
        var out = new ByteArrayOutputStream();
        given(outputStreamProvider.getOutputStream()).willReturn(new PrintStream(out));

        io.printString(EXPECTED_STRING_FOR_OUTPUT);
        var actualString = out.toString();
        assertThat(actualString).isEqualTo(EXPECTED_STRING_FOR_OUTPUT);
    }

    @DisplayName("читать строку с того потока, который предоставляет провайдер")
    @Test
    void shouldInputExpectedStringFromGivenByProviderStream() {
        var expectedString1 = "First input";
        var is = new ByteArrayInputStream(expectedString1.getBytes(StandardCharsets.UTF_8));
        given(inputStreamProvider.getInputStream()).willReturn(is);
        String actualString1 = io.inputString();
        assertThat(actualString1).isEqualTo(expectedString1);

        var expectedString2 = "Second input";
        is = new ByteArrayInputStream(expectedString2.getBytes(StandardCharsets.UTF_8));
        given(inputStreamProvider.getInputStream()).willReturn(is);
        String actualString2 = io.inputString();
        assertThat(actualString2).isEqualTo(expectedString2);
    }
}