package Hotel.Exception;

public class NonExistingEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NonExistingEntityException(String string) {
        super(string);
    }
}
