package Cinema.Exception;

/**
 * Thrown when a repository cannot retrieve an entity.
 */
public class RepositoryNotFoundException extends RuntimeException {
    public RepositoryNotFoundException(String message) {
        super(message);
    }
}
