package ru.stvort.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ru.stvort.menu.MenuLoop;

public class CliRunner implements ApplicationRunner, Ordered {

    private final int order;
    private final MenuLoop menuLoop;

    public CliRunner(int order, MenuLoop menuLoop) {
        this.order = order;
        this.menuLoop = menuLoop;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        menuLoop.startMenuLoop();
    }

    @Override
    public int getOrder() {
        return order;
    }
}
