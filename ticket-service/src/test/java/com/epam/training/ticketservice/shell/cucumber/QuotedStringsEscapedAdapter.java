package com.epam.training.ticketservice.shell.cucumber;

import org.springframework.shell.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuotedStringsEscapedAdapter implements Input {

    private static final Pattern REGEX = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");

    private final String raw;

    private final List<String> words;

    private QuotedStringsEscapedAdapter(String raw, List<String> words) {
        this.raw = raw;
        this.words = words;
    }

    public static QuotedStringsEscapedAdapter of(String raw) {
        var words = splitOneLineOfInputWithSingleDoubleQuotes(raw);
        return new QuotedStringsEscapedAdapter(raw, words);
    }

    private static List<String> splitOneLineOfInputWithSingleDoubleQuotes(String line) {
        var matchList = new ArrayList<String>();
        Matcher regexMatcher = REGEX.matcher(line);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                matchList.add(regexMatcher.group(2));
            } else {
                matchList.add(regexMatcher.group());
            }
        }
        return matchList;
    }

    @Override
    public String rawText() {
        return raw;
    }

    @Override
    public List<String> words() {
        return words;
    }
}
