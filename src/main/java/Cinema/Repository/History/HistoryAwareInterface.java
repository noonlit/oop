package Cinema.Repository.History;

/**
 * Interface that constrains all implementing classes to implement undo and redo operations.
 */
public interface HistoryAwareInterface {
    /**
     * @return true if the last operation was successfully undone, false otherwise.
     */
    boolean undo();

    /**
     * @return true if the last operation was succesfully redone, false otherwise.
     */
    boolean redo();
}
