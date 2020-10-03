package Cinema.Repository.Command;

/**
 * To be implemented by repository commands.
 */
public interface CommandInterface {
    /**
     * Executes a command.
     * @return whether the command succeeded.
     */
    boolean execute();
}
