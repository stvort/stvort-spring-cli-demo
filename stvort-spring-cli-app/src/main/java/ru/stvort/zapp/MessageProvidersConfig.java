package ru.stvort.zapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.stvort.menu.providers.MenuMessageProvider;
import ru.stvort.menu.providers.MenuMessageTypes;

@Configuration
public class MessageProvidersConfig {
    @Bean
    public MenuMessageProvider promptProvider() {
        return new MenuMessageProvider() {
            @Override
            public String getMessage() {
                return "КастомныйПромпт>";
            }

            @Override
            public MenuMessageTypes getType() {
                return MenuMessageTypes.PROMPT;
            }
        };
    }

    @Bean
    public MenuMessageProvider greetingProvider() {
        return new MenuMessageProvider() {
            @Override
            public String getMessage() {
                return "Добро пожаловать!";
            }

            @Override
            public MenuMessageTypes getType() {
                return MenuMessageTypes.GREETING;
            }
        };
    }

    @Bean
    public MenuMessageProvider goodbyeProvider() {
        return new MenuMessageProvider() {
            @Override
            public String getMessage() {
                return "Пока гады!";
            }

            @Override
            public MenuMessageTypes getType() {
                return MenuMessageTypes.GOODBYE;
            }
        };
    }
}
