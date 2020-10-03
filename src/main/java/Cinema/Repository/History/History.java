package Cinema.Repository.History;

import Cinema.Repository.Command.ReversibleCommandInterface;

import java.util.Stack;

/**
 * This class keeps track of commands (e.g. add, delete) and enables us to retrieve the last one.
 */
public class History {
    /**
     * Commands, represented as a stack, as we only ever need to undo/redo the last one.
     * We could also implement this as a list and revert the state to a certain previous point (index).
     */
    Stack<ReversibleCommandInterface> records = new Stack<>();

    /**
     * Adds the given command to the stack.
     *
     * @param command A command like add, delete, etc.
     */
    public void recordCommand(ReversibleCommandInterface command) {
        records.push(command);
    }

    /**
     * @return The last recorded command.
     */
    public ReversibleCommandInterface getLastCommand() {
        if (records.empty()) {
            return null;
        }

        return records.pop();
    }
}
