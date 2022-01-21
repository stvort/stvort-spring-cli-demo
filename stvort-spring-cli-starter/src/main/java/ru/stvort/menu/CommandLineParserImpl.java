package ru.stvort.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that parse the command line into a list of arguments
 *
 * @author Alxander Orudzhev
 */
public class CommandLineParserImpl implements CommandLineParser {

    private static final Pattern PATTERN = Pattern.compile("[^\\s\"]+|\"([^\"]*(\\\\\")*[^\"]*)+[^\\\\]\"",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    /**
     * Parse the command line into a list of arguments
     * @param commandLine - command line to parse
     * @return list of arguments where command alias is the first element
     */
    @Override
    public List<String> parse(String commandLine) {
        var matcher = PATTERN.matcher(commandLine);
        var matches = new ArrayList<String>();
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
