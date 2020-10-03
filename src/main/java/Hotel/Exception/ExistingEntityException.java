package Hotel.Exception;

public class ExistingEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExistingEntityException(String string) {
        super(string);
    }
}
