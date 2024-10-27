package backend.academy.analyzer;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

/**
 * Nginx log injector from the local path (pattern) or URL.
 * Supports pre-checking.
 *
 * @author alnmlbch
 */
@UtilityClass
public final class NginxVisitor {

    /**
     * Classic http url regex.
     */
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://\\S+$");
    /**
     * Base timeout for requests.
     */
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /**
     * Log files extension.
     */
    private static final String LOG_EXTENSION = ".log";
    /**
     * Glob syntax prefix.
     */
    private static final String GLOB = "glob:";

    public static @NonNull List<NginxLog> getLogs(final @NonNull String path)
        throws IOException, InterruptedException {
        if (isURL(path)) {
            return getLogsFromURL(path);
        }
        return getLogsFromLocalFiles(Path.of(path));
    }

    /**
     * Take logs from URL.
     *
     * @param url URL
     * @return taken logs
     * @throws IOException          if local path (or URL) is invalid
     * @throws InterruptedException if logs cannot be taken from the URL
     */
    public static @NonNull List<NginxLog> getLogsFromURL(final @NonNull String url)
        throws IOException, InterruptedException {
        try (
            var client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .cookieHandler(new CookieManager())
                .connectTimeout(TIMEOUT)
                .build()
        ) {
            final var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_2)
                .timeout(TIMEOUT)
                .GET()
                .build();
            final var response = client.send(request, HttpResponse.BodyHandlers.ofString(
                StandardCharsets.UTF_8
            ));
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                return response.body()
                    .lines()
                    .map(NginxLog::parse)
                    .toList();
            } else {
                return Collections.emptyList();
            }
        }
    }

    /**
     * Take logs from local path (pattern).
     *
     * @param path local path
     * @return taken logs
     * @throws IOException if local path (or URL) is invalid
     */
    public static @NonNull List<NginxLog> getLogsFromLocalFiles(final @NonNull Path path)
        throws IOException {
        if (Files.isRegularFile(path)) {
            return linesFromFile(path);
        }
        Path root;
        Predicate<Path> matcher;
        if (Files.isDirectory(path)) {
            root = path;
            matcher = _ -> true;
        } else {
            root = path;
            for (int lvl = 1; lvl < path.getNameCount(); lvl++) {
                root = root.getParent();
            }
            matcher = file -> FileSystems
                .getDefault()
                .getPathMatcher(globPath(path))
                .matches(file);
        }
        try (var files = Files.walk(root)) {
            return files
                .filter(Files::isRegularFile)
                .filter(matcher.and(NginxVisitor::logMatches))
                .map(NginxVisitor::linesFromFile)
                .flatMap(Collection::stream)
                .toList();
        }
    }

    /**
     * Helper method to read lines from a file.
     * Does not throw any exception. Instead, returns empty list.
     *
     * @param path file path
     * @return list of lines from file
     */
    public static @NonNull List<NginxLog> linesFromFile(final @NonNull Path path) {
        try (var files = Files.lines(path, StandardCharsets.UTF_8)) {
            return files
                .map(NginxLog::parse)
                .toList();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static boolean logMatches(final @NonNull Path path) {
        return Files.isRegularFile(path) && path.getFileName().toString().endsWith(LOG_EXTENSION);
    }

    private static @NonNull String globPath(final @NonNull Path path) {
        return GLOB + path;
    }

    /**
     * Checks correct URL.
     *
     * @param url url
     * @return {@code true} if url is correct, else {@code false}
     */
    public static boolean isURL(final @NonNull String url) {
        return URL_PATTERN.matcher(url).matches();
    }
}
