package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalSources.ALICE;
import static seedu.address.testutil.TypicalSources.HOON;
import static seedu.address.testutil.TypicalSources.IDA;
import static seedu.address.testutil.TypicalSources.getTypicalSourceManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlySourceManager;
import seedu.address.model.SourceManager;

public class JsonSourceManagerStorageTest {
    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSourceManagerStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readSourceManager_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readSourceManager(null);
    }

    private java.util.Optional<ReadOnlySourceManager> readSourceManager(String filePath) throws Exception {
        return new JsonSourceManagerStorage(Paths.get(filePath))
                .readSourceManager(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readSourceManager("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readSourceManager("notJsonFormatSourceManager.json");

        // IMPORTANT: Any code below an exceptions-throwing line (like the one above) will be ignored.
        // That means you should not have more than one exceptions test in one method
    }

    @Test
    public void readSourceManager_invalidPersonSourceManager_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readSourceManager("invalidSourceSourceManager.json");
    }

    @Test
    public void readSourceManager_invalidAndValidPersonSourceManager_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readSourceManager("invalidAndValidSourceSourceManager.json");
    }

    @Test
    public void readAndSaveSourceManager_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempSourceManager.json");
        SourceManager original = getTypicalSourceManager();
        JsonSourceManagerStorage jsonSourceManagerStorage = new JsonSourceManagerStorage(filePath);

        // Save in new file and read back
        jsonSourceManagerStorage.saveSourceManager(original, filePath);
        ReadOnlySourceManager readBack = jsonSourceManagerStorage.readSourceManager(filePath).get();
        assertEquals(original, new SourceManager(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addSource(HOON);
        original.removeSource(ALICE);
        jsonSourceManagerStorage.saveSourceManager(original, filePath);
        readBack = jsonSourceManagerStorage.readSourceManager(filePath).get();
        assertEquals(original, new SourceManager(readBack));

        // Save and read without specifying file path
        original.addSource(IDA);
        jsonSourceManagerStorage.saveSourceManager(original); // file path not specified
        readBack = jsonSourceManagerStorage.readSourceManager().get(); // file path not specified
        assertEquals(original, new SourceManager(readBack));

    }

    @Test
    public void saveSourceManager_nullSourceManager_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveSourceManager(null, "SomeFile.json");
    }

    /**
     * Saves {@code sourceManager} at the specified {@code filePath}.
     */
    private void saveSourceManager(ReadOnlySourceManager sourceManager, String filePath) {
        try {
            new JsonSourceManagerStorage(Paths.get(filePath))
                    .saveSourceManager(sourceManager, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveSourceManager_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveSourceManager(new SourceManager(), null);
    }
}
