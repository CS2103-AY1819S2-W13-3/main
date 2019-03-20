package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;

import org.junit.Test;

import seedu.address.testutil.EditSourceDescriptorBuilder;

public class EditSourceDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditCommand.EditSourceDescriptor descriptorWithSameValues = new EditCommand.EditSourceDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different title -> returns false
        EditCommand.EditSourceDescriptor editedAmy =
                new EditSourceDescriptorBuilder(DESC_AMY).withTitle(VALID_TITLE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different type -> returns false
        editedAmy = new EditSourceDescriptorBuilder(DESC_AMY).withType(VALID_TYPE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different detail -> returns false
        editedAmy = new EditSourceDescriptorBuilder(DESC_AMY).withDetail(VALID_DETAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditSourceDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_BAR).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }
}
