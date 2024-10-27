package backend.academy.analyzer;

import backend.academy.analyzer.out.Renderer;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

/**
 * Nginx logs analyzer applicator.
 * Supports different options with picocli.
 *
 * @author alnmlbch
 */
@UtilityClass
public final class Application {

    public static void main(final String[] args) {
        new CommandLine(Command.class).execute(args);
    }

    @CommandLine.Command
    @Log4j2
    private static class Command implements Callable<Void> {

        @CommandLine.Option(names = {"--path", "-p"}, required = true, description = "path to NGINX log files")
        String path;

        @CommandLine.Option(names = {"--from", "-f"}, defaultValue = Dates.MIN_DATE, description = "from what time")
        String from;

        @CommandLine.Option(names = {"--to", "-t"}, defaultValue = Dates.MAX_DATE, description = "before what time")
        String to;

        @CommandLine.Option(names = {"--out", "-o"}, defaultValue = "markdown", description = "output format")
        String format;

        @CommandLine.Option(names = "--file", defaultValue = "report", description = "output filename")
        String file;

        @CommandLine.Option(names = "--filter-user", defaultValue = "__user__", description = "users filter")
        String userFilter;

        @CommandLine.Option(names = "--filter-request", defaultValue = "__request__", description = "requests filter")
        String requestFilter;

        @Override
        public Void call() {
            log.info("Executing");
            try {
                final var renderer = Renderer.of(format);
                final var stringReport = renderer.render(Statistics.of(
                    path,
                    NginxVisitor.getLogs(path),
                    Dates.parseDate(from),
                    Dates.parseDate(to),
                    userFilter,
                    requestFilter
                ));
                final var outPath = Path.of(file + renderer.extension());
                if (!Files.exists(outPath)) {
                    Files.createFile(outPath);
                }
                try (var writer = Files.newBufferedWriter(outPath)) {
                    writer.write(stringReport);
                    writer.newLine();
                }
                log.info("Done");
            } catch (ConnectException | InterruptedException e) {
                log.error("Your internet connection is unstable: {}", e.toString());
            } catch (IOException e) {
                log.error("Input output stream error: {}", e.toString());
            } catch (Exception e) {
                log.error("Exception occurred: {}", e.toString());
            }
            return null;
        }
    }
}
