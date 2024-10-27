package backend.academy.analyzer.out;

import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * Markdown format renderer.
 *
 * @author alnmlbch
 */
public class MarkdownRenderer extends AbstractRenderer {

    @Override
    public @NonNull String extension() {
        return ".md";
    }

    @Override
    public void header(final @NonNull String label) {
        file.append("### %s%n".formatted(label));
    }

    @Override
    public void tableHeader(final @NonNull List<String> columns) {
        tableRow(columns);
        file.append('|').append(String.join(
            "|",
            columns.stream()
                .map(String::length)
                .map(len -> len + 2)
                .map("-"::repeat)
                .toList()
        )).append("|\n");
    }

    @Override
    public void tableRow(final @NonNull List<String> columns) {
        file.append("| %s |%n".formatted(String.join(" | ", columns)));
    }

    @Override
    public void tableFooter() {
    }
}
