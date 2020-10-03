package Hotel.Exception;

public class InvalidCheckinException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidCheckinException(String string) {
        super(string);
    }
}
