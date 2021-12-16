package ru.stvort.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import ru.stvort.menu.MenuLoop;

public class CliRunner implements ApplicationRunner, Ordered {

    private final int order;
    private final MenuLoop menuLoop;

    public CliRunner(int order, MenuLoop menuLoop) {
        this.order = order;
        this.menuLoop = menuLoop;
    }

    @Override
    public void run(ApplicationArguments args) {
        menuLoop.startMenuLoop();
    }

    @Override
    public int getOrder() {
        return order;
    }
}
