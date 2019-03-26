package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.model.source.Source;
import seedu.address.model.source.UniqueSourceList;

/**
 * Wraps all data at the source manager level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class SourceManager implements ReadOnlySourceManager {

    private final UniqueSourceList sources;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        sources = new UniqueSourceList();
    }

    public SourceManager() {}

    /**
     * Creates a SourceManager using the Sources in the {@code toBeCopied}
     */
    public SourceManager(ReadOnlySourceManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the source list with {@code sources}.
     * {@code sources} must not contain duplicate sources.
     */
    public void setSources(List<Source> sources) {
        this.sources.setSources(sources);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code SourceManager} with {@code newData}.
     */
    public void resetData(ReadOnlySourceManager newData) {
        requireNonNull(newData);

        setSources(newData.getSourceList());
    }

    //// source-level operations

    /**
     * Returns true if a source with the same identity as {@code source} exists in the source manager.
     */
    public boolean hasSource(Source source) {
        requireNonNull(source);
        return sources.contains(source);
    }

    /**
     * Adds a source to the source manager.
     * The source must not already exist in the source manager.
     */
    public void addSource(Source s) {
        sources.add(s);
        indicateModified();
    }

    /**
     * Adds a source to the source manager.
     * The source must not already exist in the source manager.
     */
    public void addSourceAtIndex(Source s, int index) {
        sources.addAtIndex(s, index);
        indicateModified();
    }

    /**
     * Replaces the given source {@code target} in the list with {@code editedSource}.
     * {@code target} must exist in the source manager.
     * The source identity of {@code editedSource} must not be the
     * same as another existing source in the source manager.
     */
    public void setSource(Source target, Source editedSource) {
        requireNonNull(editedSource);

        sources.setSource(target, editedSource);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code SourceManager}.
     * {@code key} must exist in the source manager.
     */
    public void removeSource(Source key) {
        sources.remove(key);
        indicateModified();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the source manager has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return sources.asUnmodifiableObservableList().size() + " sources";
        // TODO: refine later
    }

    @Override
    public ObservableList<Source> getSourceList() {
        return sources.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SourceManager // instanceof handles nulls
                && sources.equals(((SourceManager) other).sources));
    }

    @Override
    public int hashCode() {
        return sources.hashCode();
    }
}
