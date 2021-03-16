package org.quiltmc.gradleconventionplugins

import com.diffplug.spotless.FormatterFunc
import com.diffplug.spotless.FormatterStep

import java.util.regex.Pattern

class BraceSpacingStep {
    static FormatterStep create() {
        return FormatterStep.create("braces formatter", new State(), State.&toFormatter)
    }


    private static final class State implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L // increment me when changed

        private static final def beforePattern = Pattern.compile("(?<=\\n)([\\t]+)([^\\/\\r\\n \\t][^\\r\\n]*|\\/[^\\/\\r\\n][^\\r\\n]*|[^\\/\\r\\n][^\\r\\n]*(\\r?\\n\\1\\/\\/[^\\r\\n]*)+)\\r?\\n\\1(|(if|do|while|for|try)[^\\r\\n]+)\\{[\\t ]*\\r?\\n")
        private static final def afterPattern = Pattern.compile("(?<=\\n)([\\t]+)(\\})\\r?\\n\\1(?:[^\\r\\n\\}cd]|c[^\\r\\na]|ca[^\\r\\ns]|d[^\\r\\ne]|de[^\\r\\nf])")

        static FormatterFunc toFormatter(State state) {
            return { str ->
                def before = beforePattern.matcher(str)
                def after = afterPattern.matcher(str)
                def newLine = str.contains("\r") ? "\r\n" : "\n"

                before.replaceAll { input ->
                    str = str.substring(0, input.end(2) + 1) +
                            newLine + str.substring(input.end(2) + 1)
                    // replacing sections of the string somehow made gradle run out of heap space, so we just use this as a hacked foreach
                    input.group()
                }

                after.replaceAll { input ->
                    str = str.substring(0, (input.end(2) + 1)) +
                            newLine + str.substring((input.end(2) + 1))
                    // replacing sections of the string somehow made gradle run out of heap space, so we just use this as a hacked foreach
                    input.group()
                }
            }
        }
    }
}
