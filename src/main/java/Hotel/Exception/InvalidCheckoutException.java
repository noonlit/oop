package Hotel.Exception;

public class InvalidCheckoutException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidCheckoutException(String string) {
        super(string);
    }
}
