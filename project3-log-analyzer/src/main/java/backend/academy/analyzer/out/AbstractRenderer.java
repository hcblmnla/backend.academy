package backend.academy.analyzer.out;

import backend.academy.analyzer.Dates;
import backend.academy.analyzer.Statistics;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

/**
 * Abstract class for all renderers.
 * Contains the main logic of them.
 *
 * @author alnmlbch
 */
@NoArgsConstructor
public abstract class AbstractRenderer implements Renderer {

    /**
     * Strings accumulator.
     */
    protected final StringBuilder file = new StringBuilder();

    /**
     * Renders compact date.
     *
     * @param date date
     * @return compact date string
     */
    private static @NonNull String date(final @NonNull OffsetDateTime date) {
        if (Dates.MIN_UTC_TIME.equals(date) || Dates.MAX_UTC_TIME.equals(date)) {
            return "-";
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private static @NonNull String number(final long n) {
        return Long.toString(n);
    }

    private static @NonNull String bits(final long bits) {
        return "%sb".formatted(number(bits));
    }

    /**
     * Header formatter.
     *
     * @param label header label
     */
    public abstract void header(@NonNull String label);

    /**
     * Table header formatter.
     *
     * @param labels table header labels names
     */
    public abstract void tableHeader(@NonNull List<String> labels);

    /**
     * Table row formatter.
     *
     * @param columns cells in one row
     */
    public abstract void tableRow(@NonNull List<String> columns);

    /**
     * Special footer for tables.
     */
    public abstract void tableFooter();

    @Override
    @SuppressWarnings("MultipleStringLiterals")
    public @NonNull String render(final @NonNull Statistics statistics) {
        header("General information");
        newLine();
        table(2, List.of(
            "Metric", "Value",
            "Source", statistics.source(),
            "Date from", date(statistics.from()),
            "Date to", date(statistics.to()),
            "Requests count", number(statistics.size()),
            "Average size of response", bits(statistics.averageResponseSize()),
            "95 percentile of response", bits(statistics.percentileResponseSize())
        ));
        statistic(
            "Most frequent resources",
            List.of("Resource", "Count"),
            statistics.topStatistics().resources()
        );
        statistic(
            "Most frequent response codes",
            List.of("Code", "Description", "Count"),
            statistics.topStatistics().responsesCodes(),
            entry -> List.of(
                entry.getKey().toString(),
                Statistics.responseCodeDescription(entry.getKey()),
                entry.getValue().toString()
            )
        );
        statistic(
            "Most frequent request types",
            List.of("Type", "Count"),
            statistics.topStatistics().partialCounts()
        );
        statistic(
            "Most frequent remote users",
            List.of("User", "Count"),
            statistics.topStatistics().users()
        );
        return file.toString();
    }

    private <K> void statistic(
        final @NonNull String header,
        final @NonNull List<String> labels,
        final @NonNull List<Map.Entry<K, Integer>> top
    ) {
        statistic(header, labels, top, entry -> List.of(
            entry.getKey().toString(),
            entry.getValue().toString()
        ));
    }

    /**
     * Render specific statistic.
     *
     * @param <K>         type of keys
     * @param header      header of the statistic
     * @param labels      labels names
     * @param top         statistic container
     * @param entryMapper container mapper
     */
    private <K> void statistic(
        final @NonNull String header,
        final @NonNull List<String> labels,
        final @NonNull List<Map.Entry<K, Integer>> top,
        final @NonNull Function<Map.Entry<K, Integer>, List<String>> entryMapper
    ) {
        newLine();
        header(header);
        newLine();
        final List<String> table = new ArrayList<>(labels);
        table.addAll(
            top.stream()
                .map(entryMapper)
                .flatMap(Collection::stream)
                .toList()
        );
        table(labels.size(), table);
    }

    /**
     * Easy-table builder.
     *
     * @param columns number of columns
     * @param cells   table cells
     */
    private void table(final int columns, final @NonNull List<String> cells) {
        tableHeader(cells.subList(0, columns));
        for (int i = columns; i < cells.size(); i += columns) {
            tableRow(cells.subList(i, i + columns));
        }
        tableFooter();
    }

    protected void newLine() {
        file.append('\n'); // especially not System.lineSeparator() for consistent lines
    }

    public @NonNull String flush() {
        final var flushed = file.toString();
        file.setLength(0);
        return flushed;
    }
}
