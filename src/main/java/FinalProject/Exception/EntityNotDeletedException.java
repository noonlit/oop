package FinalProject.Exception;

public class EntityNotDeletedException extends RuntimeException {
    public EntityNotDeletedException(String message) {
        super(message);
    }
}