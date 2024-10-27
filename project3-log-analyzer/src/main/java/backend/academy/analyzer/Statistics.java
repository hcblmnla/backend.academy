package backend.academy.analyzer;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NonNull;

/**
 * Nginx logs statistics record.
 *
 * @param source                 nginx logs source
 * @param from                   time's lower bound
 * @param to                     time's upper bound
 * @param averageResponseSize    average response size
 * @param percentileResponseSize 95 percentile response size
 * @param topStatistics          some useful statistics
 * @param size                   logs count
 * @author alnmlbch
 */
public record Statistics(
    @NonNull String source,
    @NonNull OffsetDateTime from,
    @NonNull OffsetDateTime to,
    long averageResponseSize,
    long percentileResponseSize,
    @NonNull TopStatistics topStatistics,
    long size
) {

    /**
     * Parses the statistics from list of logs.
     *
     * @param source        nginx logs source
     * @param logs          list of logs
     * @param from          time's lower bound
     * @param to            time's upper bound
     * @param userFilter    username what should be filtered
     * @param requestFilter request type what should be filtered
     * @return statistics from desired list of logs
     */
    @SuppressWarnings("MagicNumber")
    public static @NonNull Statistics of(
        final @NonNull String source,
        final @NonNull List<NginxLog> logs,
        final @NonNull OffsetDateTime from,
        final @NonNull OffsetDateTime to,
        final @NonNull String userFilter,
        final @NonNull String requestFilter
    ) {
        final var maps = new ReducedMaps();
        final var responses = logs.stream()
            .filter(log -> Dates.inRange(from, to, log.timeLocal()))
            .filter(log -> !log.remoteUser().equalsIgnoreCase(userFilter))
            .filter(log -> !log.request().type().equals(requestFilter))
            .peek(maps::update)
            .map(NginxLog::bytes)
            .sorted()
            .toList();
        final int size = responses.size();
        final long average = responses.stream()
            .map(BigInteger::valueOf)
            .reduce(BigInteger::add)
            .orElse(BigInteger.ZERO)
            .divide(BigInteger.valueOf(size))
            .longValue();
        long percentile = responses.get(95 * size / 100);
        return new Statistics(source, from, to, average, percentile, maps.mostFrequent(), size);
    }

    /**
     * Response code information getter.
     *
     * @param code response code
     * @return information of this code
     */
    @SuppressWarnings("MagicNumber")
    public static String responseCodeDescription(final short code) {
        return switch (code / 100) {
            case 1 -> "informational";
            case 2 -> "successful";
            case 3 -> "redirection";
            case 4 -> "client error";
            case 5 -> "server error";
            default -> "unknown";
        };
    }

    public record TopStatistics(
        @NonNull List<Map.Entry<String, Integer>> resources,
        @NonNull List<Map.Entry<Short, Integer>> responsesCodes,
        @NonNull List<Map.Entry<String, Integer>> partialCounts,
        @NonNull List<Map.Entry<String, Integer>> users
    ) {
    }

    /**
     * Statistic accumulator.
     *
     * @param resources      requested resources
     * @param responsesCodes responses codes
     * @param partialCounts  partials requests counts
     * @param users          users
     * @author alnmlbch
     */
    public record ReducedMaps(
        @NonNull Map<String, Integer> resources,
        @NonNull Map<Short, Integer> responsesCodes,
        @NonNull Map<String, Integer> partialCounts,
        @NonNull Map<String, Integer> users
    ) {
        /**
         * Maximum count of top statistics.
         */
        private static final int TOP_STATS_AMOUNT = 3;

        public ReducedMaps() {
            this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        }

        private static <K> void incrementCount(final @NonNull Map<K, Integer> map, final @NonNull K key) {
            map.merge(key, 1, Integer::sum);
        }

        /**
         * Quick-and-dirty top-3 collector.
         *
         * @param map statistic container
         * @param <K> type of key
         * @return list of 3 or less element
         */
        private static <K> List<Map.Entry<K, Integer>> mostFrequentFrom(final @NonNull Map<K, Integer> map) {
            return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(TOP_STATS_AMOUNT)
                .toList();
        }

        /**
         * Updates statistics.
         *
         * @param log log what should be parsed
         */
        public void update(final @NonNull NginxLog log) {
            incrementCount(resources, "`%s`".formatted(log.request().resource()));
            incrementCount(responsesCodes, log.status());
            incrementCount(partialCounts, log.request().type());
            incrementCount(users, log.remoteUser());
        }

        public TopStatistics mostFrequent() {
            return new TopStatistics(
                mostFrequentFrom(resources),
                mostFrequentFrom(responsesCodes),
                mostFrequentFrom(partialCounts),
                mostFrequentFrom(users)
            );
        }
    }
}
