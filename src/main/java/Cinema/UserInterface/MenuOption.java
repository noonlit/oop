package Cinema.UserInterface;

/**
 * Represents a menu option.
 * Pairs an option text with a command to run.
 */
public class MenuOption {
    private final String optionText;
    private final Runnable command;

    public MenuOption(String optionText, Runnable command) {
        this.optionText = optionText;
        this.command = command;
    }

    public String getOptionText() {
        return optionText;
    }

    public Runnable getCommand() {
        return command;
    }
}
