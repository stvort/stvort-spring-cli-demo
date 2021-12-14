package ru.stvort.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Парсер командной строки должен ")
class CommandLineParserImplTest {

    private CommandLineParserImpl parser;

    @BeforeEach
    public void setUp() {
        parser = new CommandLineParserImpl();
    }

    @DisplayName("корректно парсить на аргументы строку с разделителем \"пробел\", " +
            "которая может содержать аргументы из нескольких слов, заключенные в двойные кавычки. " +
            "Такие аргументы тоже могут содержать кавычки экранируемые слэшом")
    @ParameterizedTest
    @MethodSource("commandLines")
    void shouldCorrectParseCommandLineDelimitedWithSpaceThatCanContainMultiWordQuotedArgs(String commandLine,
                                                                                          List<String> expectedResult) {
        List<String> actualRes = parser.parse(commandLine);
        assertThat(actualRes).containsExactlyElementsOf(expectedResult);

    }

    public static Stream<Arguments> commandLines() {
        return Stream.of(
                Arguments.of("Команда аргумент1 аргумент2 аргумент3",
                        List.of("Команда", "аргумент1", "аргумент2", "аргумент3")),

                Arguments.of("Команда аргумент1 аргумент2 аргумент3 ",
                        List.of("Команда", "аргумент1", "аргумент2", "аргумент3")),

                Arguments.of("Команда аргумент1 аргумент2 аргумент3             ",
                        List.of("Команда", "аргумент1", "аргумент2", "аргумент3")),

                Arguments.of("\"Команда\" аргумент1 \"аргумент 2\" \"аргумент3\"",
                        List.of("Команда", "аргумент1", "аргумент 2", "аргумент3")),

                Arguments.of("\"Команда\" аргумент1 \"аргумент \\\"2\\\"\" \"аргумент \\\"3\\\"\"",
                        List.of("Команда", "аргумент1", "аргумент \"2\"", "аргумент \"3\"")),

                Arguments.of("Команда \"Это одна \\\" строка\" 67 43 ВтораяСтрока \\\"ТретьяСтрока\\\"",
                        List.of("Команда", "Это одна \" строка", "67", "43", "ВтораяСтрока", "\"ТретьяСтрока\"")));
    }


}