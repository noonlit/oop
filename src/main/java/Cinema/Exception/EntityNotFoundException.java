package Cinema.Exception;

/**
 * Thrown when a repository cannot retrieve an entity.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
