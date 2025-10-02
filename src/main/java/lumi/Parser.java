package lumi;

/**
 * Parses user input into commands or tokens
 */
public class Parser {

    /**
     * Extracts the command word from a full input line
     * @param input full user input
     * @return command word as a first part
     */
    public static String getCommandWord(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        return parts[0];
    }

    /**
     * Returns the arguments
     * @param input as a user message
     * @return argument string
     */
    public static String getArguments(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }
}
