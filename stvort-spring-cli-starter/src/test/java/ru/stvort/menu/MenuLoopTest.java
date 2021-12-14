package ru.stvort.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.stvort.handlers.CliCommandsHandler;
import ru.stvort.io.CliInputOutputWorker;
import ru.stvort.menu.providers.MenuMessageProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static ru.stvort.menu.MenuLoop.*;
import static ru.stvort.menu.providers.MenuMessageTypes.*;

@DisplayName("Класс меню должен ")
class MenuLoopTest {

    private static final String ANY_CMD = "AnyCmd";
    private static final String GREETING_MESSAGE = "Greeting";
    private static final String NEW_PROMPT = "NewPrompt";
    private static final String GOODBYE_MESSAGE = "GoodBy";
    private static final String ERROR_MESSAGE = "Error";

    private CliInputOutputWorker io;
    private CliCommandsHandler cliCommandsHandler;
    private MenuMessageProvider greetingMenuMessageProvider;
    private MenuMessageProvider promptMenuMessageProvider;
    private MenuMessageProvider goodByeMenuMessageProvider;
    private MenuMessageProvider errorByeMenuMessageProvider;
    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        io = mock(CliInputOutputWorker.class);
        cliCommandsHandler = mock(CliCommandsHandler.class);

        greetingMenuMessageProvider = mock(MenuMessageProvider.class);
        promptMenuMessageProvider = mock(MenuMessageProvider.class);
        goodByeMenuMessageProvider = mock(MenuMessageProvider.class);
        errorByeMenuMessageProvider = mock(MenuMessageProvider.class);

        given(greetingMenuMessageProvider.getType()).willReturn(GREETING);
        given(promptMenuMessageProvider.getType()).willReturn(PROMPT);
        given(goodByeMenuMessageProvider.getType()).willReturn(GOODBYE);
        given(errorByeMenuMessageProvider.getType()).willReturn(ERROR);

        given(greetingMenuMessageProvider.getMessage()).willReturn(GREETING_MESSAGE);
        given(promptMenuMessageProvider.getMessage()).willReturn(NEW_PROMPT);
        given(goodByeMenuMessageProvider.getMessage()).willReturn(GOODBYE_MESSAGE);
        given(errorByeMenuMessageProvider.getMessage()).willReturn(ERROR_MESSAGE);

        given(cliCommandsHandler.handle(anyString())).willThrow(new RuntimeException(ERROR_MESSAGE));

        inOrder = inOrder(io, cliCommandsHandler, greetingMenuMessageProvider,
                promptMenuMessageProvider, goodByeMenuMessageProvider, errorByeMenuMessageProvider);
    }

    @DisplayName("корректно выполнять основной сценарий использования, " +
            "без использования кастомных провайдеров сообщений жизненного цикла меню")
    @Test
    void shouldCorrectExecutePlainLoop() {
        var menuLoop = prepareMenu();
        given(io.inputString()).willReturn(ANY_CMD).willReturn(EXIT_COMMAND);

        menuLoop.startMenuLoop();

        inOrder.verify(io, times(1)).printString(DEFAULT_PROMPT);
        inOrder.verify(io, times(1)).inputString();

        inOrder.verify(cliCommandsHandler, times(1)).handle(ANY_CMD);
        inOrder.verify(io, times(1)).printlnString(DEFAULT_ERROR_MESSAGE);
        inOrder.verify(io, times(1)).printlnString(ERROR_MESSAGE);

        inOrder.verify(io, times(1)).printString(DEFAULT_PROMPT);
        inOrder.verify(io, times(1)).inputString();
    }

    @DisplayName("корректно выполнять основной сценарий использования, " +
            "с использованием заданных кастомных провайдеров сообщений жизненного цикла меню")
    @Test
    void shouldCorrectExecuteLoopWithGivenMessageProviders() {
        var menuLoop = prepareMenu(greetingMenuMessageProvider,
                promptMenuMessageProvider, goodByeMenuMessageProvider, errorByeMenuMessageProvider);
        given(io.inputString()).willReturn(ANY_CMD).willReturn(EXIT_COMMAND);

        menuLoop.startMenuLoop();

        inOrder.verify(io, times(1)).printlnString(GREETING_MESSAGE);
        inOrder.verify(io, times(1)).printString(NEW_PROMPT);
        inOrder.verify(io, times(1)).inputString();

        inOrder.verify(cliCommandsHandler, times(1)).handle(ANY_CMD);

        inOrder.verify(io, times(2)).printlnString(ERROR_MESSAGE);


        inOrder.verify(io, times(1)).printString(NEW_PROMPT);
        inOrder.verify(io, times(1)).inputString();
        inOrder.verify(io, times(1)).printlnString(GOODBYE_MESSAGE);
    }

    private MenuLoop prepareMenu(MenuMessageProvider... menuMessageProviders) {
        return new MenuLoop(io, cliCommandsHandler,
                Arrays.stream(menuMessageProviders).collect(Collectors.toList()));
    }
}