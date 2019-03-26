package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.PanicMode;
import seedu.address.model.source.Source;
import seedu.address.model.source.exceptions.SourceNotFoundException;

/**
 * Represents the in-memory model of the source manager data.
 */
public class ModelManager implements Model, PanicMode {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private VersionedSourceManager versionedSourceManager;
    private final UserPrefs userPrefs;
    private final FilteredList<Source> filteredSources;
    private final SimpleObjectProperty<Source> selectedSource = new SimpleObjectProperty<>();
    private boolean panicMode = false;
    private VersionedSourceManager sourceManagerBackup = null;

    /**
     * Initializes a ModelManager with the given sourceManager and userPrefs.
     */
    public ModelManager(ReadOnlySourceManager sourceManager, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(sourceManager, userPrefs);

        logger.fine("Initializing with source manager: " + sourceManager + " and user prefs " + userPrefs);

        versionedSourceManager = new VersionedSourceManager(sourceManager);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredSources = new FilteredList<>(versionedSourceManager.getSourceList());
        filteredSources.addListener(this::ensureSelectedSourceIsValid);
    }

    public ModelManager() {
        this(new SourceManager(), new UserPrefs());
    }

    /**
     * Activates panic mode. Backs up source manager and displays an empty one.
     */
    public void enablePanicMode() {
        logger.fine("Enabling panic mode");

        if (panicMode) {
            // Prevent enabling panic mode twice, which causes a bug where by
            // the empty mock database is backed up and overwrites the actual database
            logger.fine("Panic mode already enabled.");
            return;
        }

        panicMode = true;
        sourceManagerBackup = new VersionedSourceManager(versionedSourceManager);
        logger.fine("Backed up source manager.");
        versionedSourceManager.resetData(new SourceManager());
        logger.fine("Reset visible source manager to an empty source manager.");
    }

    /**
     * Deactivates panic mode. Restores original source manager.
     */
    public void disablePanicMode() {
        logger.fine("Disabling panic mode");

        if (!panicMode) {
            logger.fine("Panic mode already disabled.");
            return;
        }

        panicMode = false;

        if (sourceManagerBackup == null) {
            logger.fine("Nothing to restore, sourceManagerBackup is null");
            return;
        }

        versionedSourceManager.resetData(sourceManagerBackup);
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getSourceManagerFilePath() {
        return userPrefs.getSourceManagerFilePath();
    }

    @Override
    public void setSourceManagerFilePath(Path sourceManagerFilePath) {
        requireNonNull(sourceManagerFilePath);
        userPrefs.setSourceManagerFilePath(sourceManagerFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setSourceManager(ReadOnlySourceManager sourceManager) {
        versionedSourceManager.resetData(sourceManager);
    }

    @Override
    public ReadOnlySourceManager getSourceManager() {
        return versionedSourceManager;
    }

    @Override
    public boolean hasSource(Source source) {
        requireNonNull(source);
        return versionedSourceManager.hasSource(source);
    }

    @Override
    public void deleteSource(Source target) {
        versionedSourceManager.removeSource(target);
    }

    @Override
    public void addSource(Source source) {
        versionedSourceManager.addSource(source);
        updateFilteredSourceList(PREDICATE_SHOW_ALL_SOURCES);
    }

    @Override
    public void addSourceAtIndex(Source source, int index) {
        versionedSourceManager.addSourceAtIndex(source, index);
        updateFilteredSourceList(PREDICATE_SHOW_ALL_SOURCES);
    }

    @Override
    public void setSource(Source target, Source editedSource) {
        requireAllNonNull(target, editedSource);

        versionedSourceManager.setSource(target, editedSource);
    }

    @Override
    public int getCount() {
        return filteredSources.size();
    }

    //=========== Filtered Source List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Source} backed by the internal list of
     * {@code versionedSourceManager}
     */
    @Override
    public ObservableList<Source> getFilteredSourceList() {
        return filteredSources;
    }

    @Override
    public void updateFilteredSourceList(Predicate<Source> predicate) {
        requireNonNull(predicate);
        filteredSources.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoSourceManager() {
        return versionedSourceManager.canUndo();
    }

    @Override
    public boolean canRedoSourceManager() {
        return versionedSourceManager.canRedo();
    }

    @Override
    public void undoSourceManager() {
        versionedSourceManager.undo();
    }

    @Override
    public void redoSourceManager() {
        versionedSourceManager.redo();
    }

    @Override
    public void commitSourceManager() {
        versionedSourceManager.commit();
    }

    //=========== Selected person ===========================================================================

    @Override
    public ReadOnlyProperty<Source> selectedSourceProperty() {
        return selectedSource;
    }

    @Override
    public Source getSelectedSource() {
        return selectedSource.getValue();
    }

    @Override
    public void setSelectedSource(Source source) {
        if (source != null && !filteredSources.contains(source)) {
            throw new SourceNotFoundException();
        }
        selectedSource.setValue(source);
    }

    /**
     * Ensures {@code selectedSource} is a valid source in {@code filteredSources}.
     */
    private void ensureSelectedSourceIsValid(ListChangeListener.Change<? extends Source> change) {
        while (change.next()) {
            if (selectedSource.getValue() == null) {
                // null is always a valid selected source, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedSourceReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedSource.getValue());
            if (wasSelectedSourceReplaced) {
                // Update selectedSource to its new value.
                int index = change.getRemoved().indexOf(selectedSource.getValue());
                selectedSource.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedSourceRemoved = change.getRemoved().stream()
                    .anyMatch(removedSource -> selectedSource.getValue().isSameSource(removedSource));
            if (wasSelectedSourceRemoved) {
                // Select the source that came before it in the list,
                // or clear the selection if there is no such source.
                selectedSource.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedSourceManager.equals(other.versionedSourceManager)
                && userPrefs.equals(other.userPrefs)
                && filteredSources.equals(other.filteredSources)
                && Objects.equals(selectedSource.get(), other.selectedSource.get());
    }

}
