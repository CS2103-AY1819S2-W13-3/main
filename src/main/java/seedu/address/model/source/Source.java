
package seedu.address.model.source;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.ModelManager;
import seedu.address.model.tag.Tag;

/**
 * Represents a Source in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Source {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Title title;
    private final Type type;
    private final Detail detail;

    private final Set<Tag> tags = new HashSet<>();

    public Source(Title title, Type type, Detail detail, Set<Tag> tags) {
        requireAllNonNull(title, type, detail, tags);
        this.title = title;
        this.type = type;
        this.detail = detail;
        this.tags.addAll(tags);
    }

    /**
     * A static utility method to validate a source attribute with a regex expression.
     */

    /**
     * A private utility method to validate a source attribute with a regex expression.
     * @param attribute An attribute of the source class, e.g. title.
     * @param regex A regex expression with which to validate `attribute`.
     * @return true if `attribute` is valid, and false if either `attribute` or `regex` is invalid.
     */
    public static boolean validateSourceAttribute(String attribute, String regex) {
        boolean validationResult;
        try {
            validationResult = attribute.matches(regex);
        } catch (PatternSyntaxException e) {
            logger.severe(e.toString());
            return false;
        }

        return validationResult;
    }

    public Title getTitle() {
        return title;
    }

    public Type getType() {
        return type;
    }

    public Detail getDetail() {
        return detail;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both sources of the have the same title and same detail.
     * Two sources will only be the same if they have the same title and same details.
     * This defines a weaker notion of equality between two sources.
     */
    public boolean isSameSource(Source otherSource) {
        if (otherSource.getTitle().title.equals(this.getTitle())
                && otherSource.getDetail().detail.equals(this.getDetail())) {
            return true;
        }

        return otherSource != null
                && otherSource.getTitle().equals(getTitle())
                && otherSource.getDetail().equals(getDetail());
    }

    /**
     * Returns true if both sources have the same identity and data fields.
     * This defines a stronger notion of equality between two sources.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Source)) {
            return false;
        }

        Source otherSource = (Source) other;
        return otherSource.getTitle().equals(getTitle())
                && otherSource.getType().equals(getType())
                && otherSource.getDetail().equals(getDetail())
                && otherSource.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(title, type, detail, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Title: ")
                .append(getTitle() + "\n")
                .append("Type: ")
                .append(getType() + "\n")
                .append("Detail: ")
                .append(getDetail() + "\n")
                .append("Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
