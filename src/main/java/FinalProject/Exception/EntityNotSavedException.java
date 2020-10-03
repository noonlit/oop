package FinalProject.Exception;

public class EntityNotSavedException extends RuntimeException {
    public EntityNotSavedException(String message) {
        super(message);
    }
}
