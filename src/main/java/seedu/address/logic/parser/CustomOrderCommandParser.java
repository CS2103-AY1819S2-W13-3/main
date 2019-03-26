package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.CustomOrderCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parser for the CustomOrder command which takes in a user input.
 */
public class CustomOrderCommandParser {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    /**
     * Parses the given {@code String} of arguments in the context of the CustomOrderCommand
     * and returns an CustomOrderCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CustomOrderCommand parse(String args) throws ParseException {
        logger.info("--- CustomOrderCommandParser: raw user input parameters:" + args);

        // splits the args into an array of strings
        String[] splitArgs = args.trim().split("\\s+");

        // if the string array size is not 2, means the user entered too few or too many arguments
        if (splitArgs.length != 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CustomOrderCommand.MESSAGE_USAGE));
        }

        try {
            int initialIndex = Integer.parseInt(splitArgs[0]);
            logger.info("--- CustomOrderCommandParser: the initial position is index " + initialIndex);
            try {
                int newPosition = Integer.parseInt(splitArgs[1]);
                logger.info("--- CustomOrderCommandParser: the new position is index " + newPosition);
                return new CustomOrderCommand(initialIndex, newPosition);
            } catch (NumberFormatException nfe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, CustomOrderCommand.MESSAGE_USAGE), nfe);
            }
        } catch (NumberFormatException nfe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CustomOrderCommand.MESSAGE_USAGE), nfe);
        }
    }
}
