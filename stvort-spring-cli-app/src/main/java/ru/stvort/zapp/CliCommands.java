package ru.stvort.zapp;

import ru.stvort.annotations.CliCommandMapping;
import ru.stvort.annotations.CliController;
import ru.stvort.io.CliInputOutputWorker;

@CliController
public class CliCommands {

    private final CliInputOutputWorker io;

    public CliCommands(CliInputOutputWorker io) {
        this.io = io;
    }

    @CliCommandMapping(value = {"hello-world", "hw"})
    public String helloWorld() {
        return "Привет МИР!";
    }

    @CliCommandMapping(value = {"greeting", "g"})
    public String greeting(String name) {
        return String.format("Привет %s!", name);
    }

    @CliCommandMapping(value = {"calc", "c"})
    public int calcTwoDigit(int firstDigit, int secondDigit, String prompt) {
        io.printlnString(prompt);
        return firstDigit + secondDigit;
    }
}
