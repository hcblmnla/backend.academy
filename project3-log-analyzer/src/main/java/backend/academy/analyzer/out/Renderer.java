package backend.academy.analyzer.out;

import backend.academy.analyzer.Statistics;
import org.jspecify.annotations.NonNull;

/**
 * Simple report renderer.
 * Supports different formats like markdown, adoc.
 *
 * @author alnmlbch
 */
public interface Renderer {

    /**
     * Renderer static ctor.
     *
     * @param format desired format
     * @return desired renderer type
     * @throws IllegalArgumentException if {@code format} are not supporting
     */
    static @NonNull Renderer of(final @NonNull String format) {
        return switch (format) {
            case "markdown" -> new MarkdownRenderer();
            case "adoc" -> new AdocRenderer();
            default -> throw new IllegalArgumentException("Unknown format: " + format);
        };
    }

    /**
     * Renders statistics.
     *
     * @param statistics statistics
     */
    @NonNull String render(@NonNull Statistics statistics);

    /**
     * Returns format's file extension.
     *
     * @return extension
     */
    @NonNull String extension();
}
