package ru.stvort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.stvort.menu.providers.MenuMessageProvider;
import ru.stvort.menu.providers.MenuMessageTypes;

@SpringBootApplication
public class StvortSpringCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(StvortSpringCliApplication.class, args);
	}

}
