package ru.stvort.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.stvort.handlers.exceptions.CliCommandNotFoundException;
import ru.stvort.handlers.exceptions.CliDuplicateCommandException;
import ru.stvort.handlers.exceptions.EmptyCliCommandException;
import ru.stvort.menu.CommandLineParser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Класс-обработчик команд должен ")
class CliCommandsHandlerImplTest {

    @DisplayName("корректно обрабатывать заданную, зарегистрированную команду")
    @Test
    void shouldCorrectHandleGivenRegisteredCommand() {
        var commandLineParser = mock(CommandLineParser.class);
        var cliCommandExecutor = mock(CliCommandExecutor.class);
        var cliCommandsHandler = new CliCommandsHandlerImpl(commandLineParser, cliCommandExecutor);

        var cliCommandMetaData = new CliCommandMetaData("cmd", null, null);
        var commandLine = "cmd 1 2 3";
        var parsedCommandLineParts = List.of("cmd", "1", "2", "3");
        var parsedArgs = List.of("1", "2", "3");

        given(commandLineParser.parse(commandLine)).willReturn(parsedCommandLineParts);

        cliCommandsHandler.registerCommand(cliCommandMetaData);
        cliCommandsHandler.handle(commandLine);
        verify(commandLineParser, times(1)).parse(commandLine);
        verify(cliCommandExecutor, times(1)).invokeCommandByMetaData(cliCommandMetaData, parsedArgs);
    }

    @DisplayName("кидать исключение при попытке обработать незарегистрированную команду")
    @Test
    void shouldThrowExceptionWhenHandlingUnRegisteredCommand() {
        var commandLineParser = mock(CommandLineParser.class);
        var cliCommandsHandler = new CliCommandsHandlerImpl(commandLineParser, null);
        given(commandLineParser.parse(anyString())).willReturn(List.of("commandLine"));
        assertThatCode(() -> cliCommandsHandler.handle("commandLine"))
                .isInstanceOf(CliCommandNotFoundException.class);
    }

    @DisplayName("кидать исключение при попытке обработать пустую команду")
    @Test
    void shouldThrowExceptionWhenHandlingEmptyCommand() {
        var commandLineParser = mock(CommandLineParser.class);
        var cliCommandsHandler = new CliCommandsHandlerImpl(commandLineParser, null);
        assertThatCode(() -> cliCommandsHandler.handle("commandLine"))
                .isInstanceOf(EmptyCliCommandException.class);
    }

    @DisplayName("регистрировать новую команду без выкидывания исключения")
    @Test
    void shouldRegisterNewCommandWithoutAnyExceptions() {
        var cliCommandsHandlerImpl = new CliCommandsHandlerImpl(null, null);
        assertThatCode(() -> cliCommandsHandlerImpl.registerCommand(new CliCommandMetaData("cmd", null, null)))
                .doesNotThrowAnyException();
    }

    @DisplayName("кидать исключение при попытке зарегистрировать команду повторно")
    @Test
    void shouldThrowExceptionWhenRegisterDuplicateCommand() {
        var cliCommandsHandler = new CliCommandsHandlerImpl(null, null);
        assertThatCode(() -> {
            cliCommandsHandler.registerCommand(new CliCommandMetaData("cmd", null, null));
            cliCommandsHandler.registerCommand(new CliCommandMetaData("cmd", null, null));
        }).isInstanceOf(CliDuplicateCommandException.class);
    }
}