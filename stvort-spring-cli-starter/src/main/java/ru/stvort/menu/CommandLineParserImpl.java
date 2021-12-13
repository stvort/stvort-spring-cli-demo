package ru.stvort.menu;

import java.util.ArrayList;
import java.util.List;

public class CommandLineParserImpl implements CommandLineParser {

    @Override
    public List<String> parse(String commandLine) {
        List<String> args = new ArrayList<>();
        do {
            commandLine = processOneArg(commandLine, args);
        } while (commandLine != null);
        return args;
    }

    private String processOneArg(String command, List<String> args) {
        if (command.isEmpty()) {
            return null;
        }

        int i = - 1;
        int k = 1;
        if (command.startsWith("\"")) {
            command = command.substring(1);

            i = command.indexOf('\"');
            if (i != -1) {
                if (i != 0 && command.charAt(i - 1) == '\\') {
                    i = command.indexOf('\"', i + 1);
                }
                k = (i != -1)? 2: 1;
            }
            if (i == -1) {
                i = command.indexOf(' ');
            }
            if (i == - 1) {
                i = command.length();
            }

        } else {
            i = command.indexOf(' ');

            if (i == - 1) {
                i = command.length();
            }
        }
        String newArg = command.substring(0, i);
        args.add(newArg);
        if (newArg.length() + k > command.length()) {
            return "";
        }
        return command.substring(newArg.length() + k);
    }
}
