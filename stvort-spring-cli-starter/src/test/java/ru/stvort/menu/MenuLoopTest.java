package ru.stvort.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.stvort.handlers.CliCommandsHandler;
import ru.stvort.io.CliInputOutputWorker;
import ru.stvort.menu.providers.MenuMessageProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static ru.stvort.menu.MenuLoop.*;
import static ru.stvort.menu.providers.MenuMessageTypes.*;

@DisplayName("Класс меню должен ")
@ExtendWith(MockitoExtension.class)
class MenuLoopTest {

    private static final String ANY_CMD = "AnyCmd";
    private static final String GREETING_MESSAGE = "Greeting";
    private static final String NEW_PROMPT = "NewPrompt";
    private static final String GOODBYE_MESSAGE = "GoodBy";
    private static final String ERROR_MESSAGE = "Error";

    @Mock
    private CliInputOutputWorker io;
    @Mock
    private CliCommandsHandler cliCommandsHandler;
    @Mock
    private MenuMessageProvider greetingMenuMessageProvider;
    @Mock
    private MenuMessageProvider promptMenuMessageProvider;
    @Mock
    private MenuMessageProvider goodByeMenuMessageProvider;
    @Mock
    private MenuMessageProvider errorByeMenuMessageProvider;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        given(cliCommandsHandler.handle(anyString())).willThrow(new RuntimeException(ERROR_MESSAGE));

        inOrder = inOrder(io, cliCommandsHandler, greetingMenuMessageProvider,
                promptMenuMessageProvider, goodByeMenuMessageProvider, errorByeMenuMessageProvider);
    }

    @DisplayName("корректно выполнять основной сценарий использования, " +
            "без использования кастомных провайдеров сообщений жизненного цикла меню")
    @Test
    void shouldCorrectExecutePlainLoop() {
        var menuLoop = prepareMenu(false);
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
        var menuLoop = prepareMenu(true);
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

    private MenuLoop prepareMenu(boolean useProviders) {
        var menuMessageProviders = new ArrayList<MenuMessageProvider>();
        if (useProviders) {
            given(greetingMenuMessageProvider.getType()).willReturn(GREETING);
            given(promptMenuMessageProvider.getType()).willReturn(PROMPT);
            given(goodByeMenuMessageProvider.getType()).willReturn(GOODBYE);
            given(errorByeMenuMessageProvider.getType()).willReturn(ERROR);

            given(greetingMenuMessageProvider.getMessage()).willReturn(GREETING_MESSAGE);
            given(promptMenuMessageProvider.getMessage()).willReturn(NEW_PROMPT);
            given(goodByeMenuMessageProvider.getMessage()).willReturn(GOODBYE_MESSAGE);
            given(errorByeMenuMessageProvider.getMessage()).willReturn(ERROR_MESSAGE);

            Collections.addAll(menuMessageProviders, greetingMenuMessageProvider,
                    promptMenuMessageProvider, goodByeMenuMessageProvider, errorByeMenuMessageProvider);
        }
        return new MenuLoop(io, cliCommandsHandler,menuMessageProviders);
    }
}