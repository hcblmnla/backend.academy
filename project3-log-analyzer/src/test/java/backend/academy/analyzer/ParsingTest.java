package backend.academy.analyzer;

import java.net.InetAddress;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParsingTest {

    @SneakyThrows
    private static List<NginxLog> getLogs(final String... dirs) {
        return NginxVisitor.getLogsFromLocalFiles(Path.of("src", dirs));
    }

    @Test
    void testParseDate() {
        // Given
        final var dateTimeString = "2020-01-01T00:00:00.00";
        // Then
        assertThat(Dates.parseDate(dateTimeString)).isEqualTo(
            OffsetDateTime.of(LocalDateTime.parse(dateTimeString), ZoneOffset.UTC)
        );
        assertThatThrownBy(() -> Dates.parseDate("2020-2000-20"))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "192.168.1.10 - - [14/Oct/2024:10:20:55 +0300] \"PUT /update HTTP/1.1\" 200 0 \"http://example.com/profile\"",
        "192.168.1.10 - - [14/Oct/2024:10:20:55 +0300] \"PUT /update HTTP/1.1\" 200 \"Mozilla/5.0\"",
        "192.168.1.10 - \"PUT /update HTTP/1.1\" 200 0 \"http://example.com/profile\" \"Mozilla/5.0\"",
        "IP_ADDRESS - - [14/Oct/2024:10:20:55 +0300] \"PUT /update HTTP/1.1\" 200 0 \"http://example.com/profile\" \"Mozilla/5.0\"",
        "192.168.1.10 - - [14/Oct/2024:10:20:55 +0300] \"/update HTTP/1.1\" 200 0 \"http://example.com/profile\" \"Mozilla/5.0\""
    })
    void invalidLogsShouldThrowException(final String log) {
        assertThatThrownBy(() -> NginxLog.parse(log))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @SneakyThrows
    @Test
    void visitRegularFileTest() {
        // Given
        final var fromFile = getLogs("test", "resources", "logs", "mini.log");
        final var fromDir = getLogs("test", "resources", "logs");
        final var expected = List.of(
            new NginxLog(
                InetAddress.ofLiteral("192.168.1.10"),
                "-",
                Dates.parseDate("2024-10-14T10:15:30+03:00"),
                new NginxLog.Request("GET", "/index.html"),
                (short) 200,
                1024,
                "-",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
            ),
            new NginxLog(
                InetAddress.ofLiteral("203.0.113.55"),
                "-",
                Dates.parseDate("2024-10-14T10:15:35+03:00"),
                new NginxLog.Request("POST", "/login"),
                (short) 302,
                512,
                "http://example.com/",
                "Mozilla/5.0 (X11; Linux x86_64)"
            )
        );
        // Then
        assertThat(fromFile).isEqualTo(fromDir).isEqualTo(expected);
    }

    @Test
    void visitInvalidFile() {
        assertThat(NginxVisitor.linesFromFile(Path.of("__NULL__"))).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 1000, 600})
    void unknownResponseCode(final int code) {
        assertThat(Statistics.responseCodeDescription((short) code))
            .isEqualToIgnoringCase("unknown");
    }
}
