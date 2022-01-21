package ru.stvort.zapp;

import ru.stvort.annotations.CliCommandMapping;
import ru.stvort.annotations.CliController;
import ru.stvort.handlers.transformers.CustomArg;
import ru.stvort.io.CliInputOutputWorker;
import ru.stvort.zapp.dto.UserDto;

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

    //greeting Выходные
    @CliCommandMapping(value = {"greeting", "g"})
    public String greeting(String name) {
        return String.format("Привет %s!", name);
    }

    //calc 1764 1346 Промпт
    @CliCommandMapping(value = {"calc", "c"})
    public int calcTwoDigit(int firstDigit, int secondDigit, String prompt) {
        io.printlnString(prompt);
        return firstDigit + secondDigit;
    }

    //r --firstName:Вася --lastName:Тундрин --password:12345
    @CliCommandMapping(value = {"register", "r"})
    public UserDto register(String firstName, String password, @CustomArg UserDto user, String lastName) {

        io.printlnString("firstName: " + firstName);
        io.printlnString("lastName: " + lastName);
        io.printlnString("password: " + password);
        return user;
    }
}
