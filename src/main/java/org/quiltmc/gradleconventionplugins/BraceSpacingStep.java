package org.quiltmc.gradleconventionplugins;

import com.diffplug.spotless.FormatterFunc;
import com.diffplug.spotless.FormatterStep;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Requires newlines before a statement with an opening brace, and after a statement with a closing brace
 */
public class BraceSpacingStep {
    static FormatterStep create() {
        return FormatterStep.create("braces formatter", new State(), State::toFormatter);
    }


    private static final class State implements Serializable {
        private static final long serialVersionUID = 1L; // increment me when changed

        private static final Pattern beforePattern = Pattern.compile("(?<=\\n)([\\t]+)([^\\/\\r\\n \\t][^\\r\\n]*|\\/[^\\/\\r\\n][^\\r\\n]*|[^\\/\\r\\n][^\\r\\n]*(\\r?\\n\\1\\/\\/[^\\r\\n]*)+)\\r?\\n\\1(|(if|do|while|for|try)[^\\r\\n]+)\\{[\\t ]*\\r?\\n");
        private static final Pattern afterPattern = Pattern.compile("(?<=\\n)([\\t]+)(\\})\\r?\\n\\1(?:[^\\r\\n\\}cd]|c[^\\r\\na]|ca[^\\r\\ns]|d[^\\r\\ne]|de[^\\r\\nf])");

        // TODO: i think matcher has a way to cleanly do this, but it works so
        static FormatterFunc toFormatter(State state) {
            return input -> {
                String result = input;
                Matcher before = beforePattern.matcher(input);
                Matcher after = afterPattern.matcher(input);
                String newLine = input.contains("\r") ? "\r\n" : "\n";
                int delta = 0; // we are adding characters so this isn't always the same
                while (before.find()) {
                   result = input.substring(0, before.end(2) + delta + 1) + newLine + input.substring(before.end(2) + delta + 1);
                    delta += newLine.length();
                }
                while (after.find()) {
                    result = input.substring(0, after.end(2) + delta + 1 ) + newLine + input.substring(after.end(2) + delta+ 1);
                    delta += newLine.length();
                }
                return result;
            };
        }
    }
}