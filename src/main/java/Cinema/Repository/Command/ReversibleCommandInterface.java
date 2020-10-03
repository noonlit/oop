package Cinema.Repository.Command;

public interface ReversibleCommandInterface extends CommandInterface {
    /**
     * Reverts the executed command.
     *
     * @return whether the revert succeeded.
     */
    boolean undo();

    /**
     * Reverts the undo operation.
     *
     * @return whether the revert succeeded.
     */
    boolean redo();
}
