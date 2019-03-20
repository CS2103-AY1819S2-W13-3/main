package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DETAILS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.SourceManager;
import seedu.address.model.source.Source;
import seedu.address.model.source.TitleContainsKeywordsPredicate;
import seedu.address.testutil.EditSourceDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_TITLE_AMY = "Amy Bee";
    public static final String VALID_TITLE_BOB = "Bob Choo";
    public static final String VALID_TYPE_AMY = "amy type";
    public static final String VALID_TYPE_BOB = "bob type";
    public static final String VALID_DETAIL_AMY = "amy_detail";
    public static final String VALID_DETAIL_BOB = "bob_detail";
    public static final String VALID_TAG_FOO = "foo";
    public static final String VALID_TAG_BAR = "bar";

    public static final String TITLE_DESC_AMY = " " + PREFIX_TITLE + VALID_TITLE_AMY;
    public static final String TITLE_DESC_BOB = " " + PREFIX_TITLE + VALID_TITLE_BOB;
    public static final String TYPE_DESC_AMY = " " + PREFIX_TYPE + VALID_TYPE_AMY;
    public static final String TYPE_DESC_BOB = " " + PREFIX_TYPE + VALID_TYPE_BOB;
    public static final String DETAIL_DESC_AMY = " " + PREFIX_DETAILS + VALID_DETAIL_AMY;
    public static final String DETAIL_DESC_BOB = " " + PREFIX_DETAILS + VALID_DETAIL_BOB;
    public static final String TAG_DESC_FOO = " " + PREFIX_TAG + VALID_TAG_FOO;
    public static final String TAG_DESC_BAR = " " + PREFIX_TAG + VALID_TAG_BAR;

    public static final String INVALID_TITLE_DESC = " " + PREFIX_TITLE + "!@#$%^&*()"; // Special characters not allowed
    public static final String INVALID_TYPE_DESC = " " + PREFIX_TYPE + "!@#$%^&*()"; // Special characters not allowed
    public static final String INVALID_DETAIL_DESC = " " + PREFIX_DETAILS; // empty string not allowed for details
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "foobar*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditSourceDescriptor DESC_AMY;
    public static final EditCommand.EditSourceDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditSourceDescriptorBuilder().withTitle(VALID_TITLE_AMY)
                .withType(VALID_TYPE_AMY).withDetail(VALID_DETAIL_AMY)
                .withTags(VALID_TAG_FOO).build();
        DESC_BOB = new EditSourceDescriptorBuilder().withTitle(VALID_TITLE_BOB)
                .withType(VALID_TYPE_BOB).withDetail(VALID_DETAIL_BOB)
                .withTags(VALID_TAG_FOO, VALID_TAG_BAR).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            CommandResult expectedCommandResult, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the source manager, filtered source list and selected source in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        SourceManager expectedSourceManager = new SourceManager(actualModel.getSourceManager());
        List<Source> expectedFilteredList = new ArrayList<>(actualModel.getFilteredSourceList());
        Source expectedSelectedSource = actualModel.getSelectedSource();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedSourceManager, actualModel.getSourceManager());
            assertEquals(expectedFilteredList, actualModel.getFilteredSourceList());
            assertEquals(expectedSelectedSource, actualModel.getSelectedSource());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the source at the given {@code targetIndex} in the
     * {@code model}'s source manager.
     */
    public static void showSourceAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredSourceList().size());

        Source source = model.getFilteredSourceList().get(targetIndex.getZeroBased());
        final String[] splitTitle = source.getTitle().title.split("\\s+");
        model.updateFilteredSourceList(new TitleContainsKeywordsPredicate(Arrays.asList(splitTitle[0])));

        assertEquals(1, model.getFilteredSourceList().size());
    }

    /**
     * Deletes the first source in {@code model}'s filtered list from {@code model}'s source manager.
     */
    public static void deleteFirstSource(Model model) {
        Source firstSource = model.getFilteredSourceList().get(0);
        model.deleteSource(firstSource);
        model.commitSourceManager();
    }

}
