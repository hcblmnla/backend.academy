package backend.academy.analyzer;

import java.net.InetAddress;
import java.time.OffsetDateTime;
import java.util.regex.Pattern;
import org.jspecify.annotations.NonNull;

/**
 * Standard nginx log record.
 *
 * @param remoteAddr    remote address
 * @param remoteUser    remote username
 * @param timeLocal     log's time
 * @param request       log's request
 * @param status        request status
 * @param bytes         request response size in bytes
 * @param httpRefer     log's http reference
 * @param httpUserAgent user agent
 * @author alnmlbch
 */
public record NginxLog(
    @NonNull InetAddress remoteAddr,
    @NonNull String remoteUser,
    @NonNull OffsetDateTime timeLocal,
    @NonNull Request request,
    short status,
    long bytes,
    @NonNull String httpRefer,
    @NonNull String httpUserAgent
) {

    /**
     * Standard nginx log regex.
     */
    private static final Pattern PATTERN = Pattern.compile(
        "(\\S+) - (\\S+) \\[(.+?)] \"([^\"]+)\" (\\d{3}) (\\d+) \"([^\"]*)\" \"([^\"]*)\""
    );

    /**
     * Parses nginx log from the string line.
     *
     * @param line input string
     * @return parsed nginx log
     */
    @SuppressWarnings("MagicNumber")
    public static NginxLog parse(final @NonNull String line) {
        final var matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid nginx log: " + line);
        }
        return new NginxLog(
            InetAddress.ofLiteral(matcher.group(1)),
            matcher.group(2),
            Dates.parseNginxDate(matcher.group(3)),
            Request.of(matcher.group(4)),
            Short.parseShort(matcher.group(5)),
            Long.parseLong(matcher.group(6)),
            matcher.group(7),
            matcher.group(8)
        );
    }

    /**
     * Nginx request record.
     * Needs for the best analyzing.
     *
     * @param type     request type (like a GET, POST..)
     * @param resource request resource
     * @author alnmlbch
     */
    public record Request(@NonNull String type, @NonNull String resource) {

        /**
         * Standard nginx log request regex.
         */
        private static final Pattern PATTERN = Pattern.compile("(\\S+) (\\S+) (\\S+)");

        public static Request of(final @NonNull String request) {
            final var matcher = PATTERN.matcher(request);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid nginx log request: " + request);
            }
            return new Request(matcher.group(1), matcher.group(2));
        }
    }
}
