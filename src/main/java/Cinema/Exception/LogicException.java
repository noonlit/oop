package Cinema.Exception;

/**
 * Thrown when the input doesn't make sense (e.g. start date for a booking is before end date).
 */
public class LogicException extends RuntimeException {
    public LogicException(String message) {
        super(message);
    }
}
