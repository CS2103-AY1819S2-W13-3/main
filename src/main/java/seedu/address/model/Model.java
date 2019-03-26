package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.PanicMode;
import seedu.address.model.source.Source;

/**
 * The API of the Model component.
 */
public interface Model extends PanicMode {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Source> PREDICATE_SHOW_ALL_SOURCES = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' source manager file path.
     */
    Path getSourceManagerFilePath();

    /**
     * Sets the user prefs' source manager file path.
     */
    void setSourceManagerFilePath(Path sourceManagerFilePath);

    /**
     * Replaces source manager data with the data in {@code sourceManager}.
     */
    void setSourceManager(ReadOnlySourceManager sourceManager);

    /** Returns the SourceManager */
    ReadOnlySourceManager getSourceManager();

    /**
     * Returns true if a source with the same identity as {@code source} exists in the source manager.
     */
    boolean hasSource(Source source);

    /**
     * Deletes the given source.
     * The source must exist in the source manager.
     */
    void deleteSource(Source target);

    /**
     * Adds the given source.
     * {@code source} must not already exist in the source manager.
     */
    void addSource(Source source);

    /**
     * Adds the given source to an index.
     * {@code source} must not already exist in the source manager.
     */
    void addSourceAtIndex(Source source, int index);

    /**
     * Replaces the given source {@code target} with {@code editedSource}.
     * {@code target} must exist in the source manager.
     * The source identity of {@code editedSource} must not be the same
     * as another existing source in the source manager.
     */
    void setSource(Source target, Source editedSource);

    /** Returns an unmodifiable view of the filtered source list */
    ObservableList<Source> getFilteredSourceList();

    /**
     * Updates the filter of the filtered source list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredSourceList(Predicate<Source> predicate);

    /**
     * Returns true if the model has previous source manager states to restore.
     */
    boolean canUndoSourceManager();

    /**
     * Returns true if the model has undone source manager states to restore.
     */
    boolean canRedoSourceManager();

    /**
     * Restores the model's source manager to its previous state.
     */
    void undoSourceManager();

    /**
     * Restores the model's source manager to its previously undone state.
     */
    void redoSourceManager();

    /**
     * Saves the current source manager state for undo/redo.
     */
    void commitSourceManager();

    /**
     * Selected source in the filtered source list.
     * null if no source is selected.
     */
    ReadOnlyProperty<Source> selectedSourceProperty();

    /**
     * Returns the selected source in the filtered source list.
     * null if no source is selected.
     */
    Source getSelectedSource();

    /**
     * Sets the selected source in the filtered source list.
     */
    void setSelectedSource(Source source);

    /**
     * Default implementation to prevent compilation errors when implementors of Model
     * do not implement PanicMode.
     */
    default void enablePanicMode() {}

    /**
     * Default implementation to prevent compilation errors when implementors of Model
     * do not implement PanicMode.
     */
    default void disablePanicMode() {}

    /**
     * Returns the total number of sources in the database.
     * 0 if no sources in list.
     */
    int getCount();
}
