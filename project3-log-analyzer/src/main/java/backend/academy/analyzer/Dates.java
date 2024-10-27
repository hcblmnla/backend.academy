package backend.academy.analyzer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

/**
 * Different dates parsing utility class.
 * Supports pre-checking with the tags and regexes.
 * Regexes needs for them, and it is necessary to parse different dates.
 *
 * @author alnmlbch
 */
@UtilityClass
public final class Dates {

    /**
     * Minimal utc date.
     */
    public static final OffsetDateTime MIN_UTC_TIME = OffsetDateTime.of(
        LocalDateTime.MIN,
        ZoneOffset.UTC
    );
    /**
     * Maximal utc date.
     */
    public static final OffsetDateTime MAX_UTC_TIME = OffsetDateTime.of(
        LocalDateTime.MAX,
        ZoneOffset.UTC
    );

    /**
     * Minimal date tag.
     */
    public static final String MIN_DATE = "__min_date__";
    /**
     * Maximal date tag.
     */
    public static final String MAX_DATE = "__max_date__";

    /**
     * Offset date regex.
     */
    private static final Pattern OFFSET_DATE_PATTERN = Pattern.compile(
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?\\+\\d{2}:\\d{2}$"
    );
    /**
     * Date time regex.
     */
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile(
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?$"
    );
    /**
     * Date regex.
     */
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "^\\d{4}-\\d{2}-\\d{2}$"
    );

    /**
     * Nginx log date formatter.
     */
    private static final DateTimeFormatter NGINX_DATE_FORMATTER = DateTimeFormatter.ofPattern(
        "dd/MMM/yyyy:HH:mm:ss Z",
        Locale.ENGLISH
    );

    /**
     * Parses the correct date from the string.
     *
     * @param date input string
     * @return parsed date
     */
    public static @NonNull OffsetDateTime parseDate(final @NonNull String date) {
        if (isOffsetDate(date)) {
            return OffsetDateTime.parse(date);
        }
        LocalDateTime local;
        if (isMinDate(date)) {
            local = LocalDateTime.MIN;
        } else if (isMaxDate(date)) {
            local = LocalDateTime.MAX;
        } else if (isDateTime(date)) {
            local = LocalDateTime.parse(date);
        } else if (isDate(date)) {
            local = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN);
        } else {
            throw new IllegalArgumentException("Unknown date: " + date);
        }
        return OffsetDateTime.of(local, ZoneOffset.UTC);
    }

    /**
     * Parses the nginx log date from the string.
     *
     * @param date input string
     * @return parsed date
     */
    public static @NonNull OffsetDateTime parseNginxDate(final @NonNull String date) {
        return OffsetDateTime.parse(date, NGINX_DATE_FORMATTER);
    }

    /**
     * Checks that the date is in the range.
     *
     * @param from lower bound
     * @param to   upper bound
     * @param date date
     * @return {@code true} if date in the range, else {@code false}
     */
    public static boolean inRange(
        final @NonNull OffsetDateTime from,
        final @NonNull OffsetDateTime to,
        final @NonNull OffsetDateTime date
    ) {
        return date.isAfter(from) && date.isBefore(to);
    }

    /**
     * Checks minimal date.
     *
     * @param date input string
     * @return {@code true} if input string is minimal date, else {@code false}
     */
    public static boolean isMinDate(final @NonNull String date) {
        return MIN_DATE.equals(date);
    }

    /**
     * Checks maximal date.
     *
     * @param date input string
     * @return {@code true} if input string is maximal date, else {@code false}
     */
    public static boolean isMaxDate(final @NonNull String date) {
        return MAX_DATE.equals(date);
    }

    /**
     * Checks offset date.
     *
     * @param date input string
     * @return {@code true} if input string is correct offset date, else {@code false}
     */
    public static boolean isOffsetDate(final @NonNull String date) {
        return OFFSET_DATE_PATTERN.matcher(date).matches();
    }

    /**
     * Checks date time.
     *
     * @param date input string
     * @return {@code true} if input string is correct date time, else {@code false}
     */
    public static boolean isDateTime(final @NonNull String date) {
        return DATE_TIME_PATTERN.matcher(date).matches();
    }

    /**
     * Checks date.
     *
     * @param date input string
     * @return {@code true} if input string is correct date, else {@code false}
     */
    public static boolean isDate(final @NonNull String date) {
        return DATE_PATTERN.matcher(date).matches();
    }
}
