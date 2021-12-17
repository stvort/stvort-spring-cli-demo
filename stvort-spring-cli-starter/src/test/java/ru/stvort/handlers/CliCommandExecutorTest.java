package ru.stvort.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс для выполнения команд должен ")
class CliCommandExecutorTest {

    @DisplayName("корректно выполнять заданную команду ")
    @ParameterizedTest
    @MethodSource("testParams")
    void shouldCorrectExecuteGivenCommand(CliCommandMetaData metaData, List<String> args, String expectedResult) {
        var cliCommandExecutor = new CliCommandExecutor();
        Object actualResult = cliCommandExecutor.invokeCommandByMetaData(metaData, args);
        assertThat(actualResult.toString()).isEqualTo(expectedResult);
    }



    public static Stream<Arguments> testParams() throws NoSuchMethodException {
        return Stream.of(
                Arguments.of(makeCliCommandMetaData(), List.of(), "cmd1"),
                Arguments.of(makeCliCommandMetaData(String.class), List.of("0"), "0"),
                Arguments.of(makeCliCommandMetaData(byte.class), List.of("1"), "1"),
                Arguments.of(makeCliCommandMetaData(Byte.class), List.of("2"), "2"),
                Arguments.of(makeCliCommandMetaData(short.class), List.of("3"), "3"),
                Arguments.of(makeCliCommandMetaData(Short.class), List.of("4"), "4"),
                Arguments.of(makeCliCommandMetaData(int.class), List.of("5"), "5"),
                Arguments.of(makeCliCommandMetaData(Integer.class), List.of("6"), "6"),
                Arguments.of(makeCliCommandMetaData(long.class), List.of("7"), "7"),
                Arguments.of(makeCliCommandMetaData(Long.class), List.of("8"), "8"),
                Arguments.of(makeCliCommandMetaData(boolean.class), List.of("true"), "true"),
                Arguments.of(makeCliCommandMetaData(Boolean.class), List.of("false"), "false"),
                Arguments.of(makeCliCommandMetaData(String.class, byte.class, Byte.class, short.class, Short.class,
                                int.class, Integer.class, long.class, Long.class, boolean.class, Boolean.class),
                        List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "true", "false"),
                        new MegaCmdResult("0", (byte)1, (byte)2, (short) 3, (short) 4, 5, 6, 7L, 8L, true, false).toString())
                );
    }

    private static CliCommandMetaData makeCliCommandMetaData(Class<?>... argsClasses) throws NoSuchMethodException {
        return new CliCommandMetaData("cmd", TestCommands.class.getMethod("cmd", argsClasses), new TestCommands());
    }

    private static class TestCommands {
        public String cmd() {
            return "cmd1";
        }

        public String cmd(String arg1) {
            return arg1;
        }

        public byte cmd(byte arg1) {
            return arg1;
        }

        public Byte cmd(Byte arg1) {
            return arg1;
        }

        public short cmd(short arg1) {
            return arg1;
        }

        public Short cmd(Short arg1) {
            return arg1;
        }

        public int cmd(int arg1) {
            return arg1;
        }

        public Integer cmd(Integer arg1) {
            return arg1;
        }

        public long cmd(long arg1) {
            return arg1;
        }

        public Long cmd(Long arg1) {
            return arg1;
        }

        public boolean cmd(boolean arg1) {
            return arg1;
        }

        public Boolean cmd(Boolean arg1) {
            return arg1;
        }

        public MegaCmdResult cmd(String arg1, byte arg2, Byte arg3, short arg4, Short arg5,
                                 int arg6, Integer arg7, long arg8, Long arg9, boolean arg10, Boolean arg11) {
            return new MegaCmdResult(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
        }
    }

    private static class MegaCmdResult {
        private final String arg1;
        private final byte arg2;
        private final Byte arg3;
        private final short arg4;
        private final Short arg5;
        private final int arg6;
        private final Integer arg7;
        private final long arg8;
        private final Long arg9;
        private final boolean arg10;
        private final Boolean arg11;

        public MegaCmdResult(String arg1, byte arg2, Byte arg3, short arg4, Short arg5, int arg6, Integer arg7,
                             long arg8, Long arg9, boolean arg10, Boolean arg11) {
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
            this.arg4 = arg4;
            this.arg5 = arg5;
            this.arg6 = arg6;
            this.arg7 = arg7;
            this.arg8 = arg8;
            this.arg9 = arg9;
            this.arg10 = arg10;
            this.arg11 = arg11;
        }

        @Override
        public String toString() {
            return "MegaCmdResult{" +
                    "arg1='" + arg1 + '\'' +
                    ", arg2=" + arg2 +
                    ", arg3=" + arg3 +
                    ", arg4=" + arg4 +
                    ", arg5=" + arg5 +
                    ", arg6=" + arg6 +
                    ", arg7=" + arg7 +
                    ", arg8=" + arg8 +
                    ", arg9=" + arg9 +
                    ", arg10=" + arg10 +
                    ", arg11=" + arg11 +
                    '}';
        }
    }

}