package backend.academy.analyzer.out;

import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * Adoc format renderer.
 *
 * @author alnmlbch
 */
public class AdocRenderer extends AbstractRenderer {

    @Override
    public @NonNull String extension() {
        return ".adoc";
    }

    @Override
    public void header(final @NonNull String label) {
        file.append("== %s%n".formatted(label));
    }

    @Override
    public void tableHeader(final @NonNull List<String> columns) {
        file.append("[cols=\"%s\", option=\"header\", frame=\"topbot\"]%n|===%n".formatted(
            String.join(",", "1".repeat(columns.size()).split(""))
        ));
        tableRow(columns);
    }

    @Override
    public void tableRow(final @NonNull List<String> columns) {
        file.append("| %s%n".formatted(String.join(" | ", columns)));
    }

    @Override
    public void tableFooter() {
        file.append("|===\n");
    }
}
