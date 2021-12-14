package ru.stvort.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineParserImpl implements CommandLineParser {

    private static final Pattern PATTERN = Pattern.compile("\"(\\\\+\"|[^\"])*?\"|(\\\\\\s|[^\\s])+",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    @Override
    public List<String> parse(String commandLine) {
        Matcher matcher = PATTERN.matcher(commandLine);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            var arg = matcher.group();
            if (arg.startsWith("\"") && arg.endsWith("\"")) {
                arg = arg.substring(1, arg.length() - 1);
            }
            arg = arg.replaceAll("\\\\\"", "\"");
            matches.add(arg);
        }
        return matches;
    }
}
