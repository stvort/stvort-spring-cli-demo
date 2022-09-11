package ru.stvort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.stvort.menu.CommandLineParserImpl;
import ru.stvort.menu.providers.MenuMessageProvider;
import ru.stvort.menu.providers.MenuMessageTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class StvortSpringCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(StvortSpringCliApplication.class, args);
	}

}
