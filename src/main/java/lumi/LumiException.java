package lumi;

/**
 * Represents an exception to Lumi
 */
public class LumiException extends Exception {

    /**
     * Creates a LumiException with a message
     * @param message which is error message
     */
    public LumiException(String message) {
        super(message);
    }
}
