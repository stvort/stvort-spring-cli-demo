package ru.stvort.menu;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.stvort.handlers.CliCommandsHandler;
import ru.stvort.io.CliInputOutputWorker;
import ru.stvort.menu.providers.MenuMessageProvider;
import ru.stvort.menu.providers.MenuMessageTypes;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuLoop {

    static final String DEFAULT_ERROR_MESSAGE = "Ошибка при обработке команды";
    static final String DEFAULT_PROMPT = "Наверное это мой промпт>";
    static final String EXIT_COMMAND = "exit";

    private final Log logger = LogFactory.getLog(getClass());

    private final CliInputOutputWorker io;
    private final CliCommandsHandler cliCommandsHandler;
    private final Map<MenuMessageTypes, MenuMessageProvider> messageProviders;

    public MenuLoop(CliInputOutputWorker io,
                    CliCommandsHandler cliCommandsHandler,
                    List<MenuMessageProvider> messageProviders) {
        this.io = io;
        this.cliCommandsHandler = cliCommandsHandler;
        this.messageProviders = messageProviders.stream()
                .collect(Collectors.toMap(MenuMessageProvider::getType, Function.identity()));
    }

    public void startMenuLoop(){
        printGreeting();

        while (true) {
            printPrompt();
            try {
                String commandLine = io.inputString();

                if (commandLine.equals(EXIT_COMMAND)) {
                    break;
                }
                var res = cliCommandsHandler.handle(commandLine);
                io.printlnString(String.valueOf(res));
            } catch (Exception e) {
                printError(e);
            }
        }

        printGoodBye();
    }

    private void printGreeting() {
        var greetingMessageProvider = messageProviders.get(MenuMessageTypes.GREETING);
        if (greetingMessageProvider != null) {
            io.printlnString(greetingMessageProvider.getMessage());
        }
    }

    private void printGoodBye() {
        var goodbyeMessageProvider = messageProviders.get(MenuMessageTypes.GOODBYE);
        if (goodbyeMessageProvider != null) {
            io.printlnString(goodbyeMessageProvider.getMessage());
        }
    }

    private void printPrompt() {
        var promptMessageProvider = messageProviders.get(MenuMessageTypes.PROMPT);
        var prompt = promptMessageProvider == null? DEFAULT_PROMPT: promptMessageProvider.getMessage();
        io.printString(prompt);
    }

    private void printError(Exception e) {
        var errorMessageProvider = messageProviders.get(MenuMessageTypes.ERROR);
        var errorMessage = errorMessageProvider == null? DEFAULT_ERROR_MESSAGE: errorMessageProvider.getMessage();
        io.printlnString(errorMessage);
        io.printlnString(e.getMessage());
    }
}
