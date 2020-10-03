package Cinema.Exception;

/**
 * Thrown when a date/datetime cannot be parsed from a given string.
 */
public class FormatException extends RuntimeException {
    public FormatException(String message) {
        super(message);
    }
}

