package FinalProject.Exception;

public class TransactionNotCompletedException extends RuntimeException {
    public TransactionNotCompletedException(String message) {
        super(message);
    }
}
