package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SOURCES;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.source.Source;

/**
 * Lists all persons in the address book to the user.
 */
public class CustomOrderCommand extends Command {
    public static final String COMMAND_WORD = "order";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Swaps the source in a given index to a position in the list.\n"
            + "Parameters: "
            + "SOURCE_INDEX (must be a positive integer) "
            + "MOVE_POSITION (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2 6";

    public static final String MESSAGE_SUCCESS = "Source at position %d moved to position %d.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private int initialIndex;
    private int newPosition;

    public CustomOrderCommand(int initialIndex, int newPosition) {
        this.initialIndex = initialIndex - 1;
        this.newPosition = newPosition - 1;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        model.updateFilteredSourceList(PREDICATE_SHOW_ALL_SOURCES);
        List<Source> completeSourceList = model.getFilteredSourceList();

        if (initialIndex >= completeSourceList.size()
                || newPosition >= completeSourceList.size()
                || initialIndex < 0
                || newPosition < 0) {
            logger.info("--- CustomOrderCommand: Index given is out of bounds.");
            throw new CommandException(Messages.MESSAGE_INVALID_SOURCE_DISPLAYED_INDEX);
        }

        if (newPosition > initialIndex) {
            newPosition--;
        }

        Source sourceToMove = completeSourceList.get(initialIndex);
        model.deleteSource(sourceToMove);
        model.addSourceAtIndex(sourceToMove, newPosition);
        model.commitSourceManager();

        return new CommandResult(String.format(MESSAGE_SUCCESS, initialIndex + 1, newPosition + 1));
    }
}
