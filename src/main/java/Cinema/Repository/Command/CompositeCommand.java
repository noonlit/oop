package Cinema.Repository.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO sensible return values.
 */
public class CompositeCommand implements ReversibleCommandInterface {
    private List<ReversibleCommandInterface> commandList = new ArrayList<>();

    /**
     * @param command
     */
    public void addCommand(ReversibleCommandInterface command) {
        this.commandList.add(command);
    }

    /**
     * Undoes every command registered in this composite.
     */
    public boolean undo() {
        for (ReversibleCommandInterface command : commandList) {
            command.undo();
        }

        return true;
    }

    /**
     * Redoes every command registered in this composite.
     */
    public boolean redo() {
        for (ReversibleCommandInterface command : commandList) {
            command.redo();
        }

        return true;
    }

    /**
     * Executes every command registered in this composite.
     */
    public boolean execute() {
        for (ReversibleCommandInterface command : commandList) {
            command.execute();
        }

        return true;
    }
}
