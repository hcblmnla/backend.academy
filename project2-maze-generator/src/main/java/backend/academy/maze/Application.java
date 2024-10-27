package backend.academy.maze;

import backend.academy.maze.io.IOHandler;
import backend.academy.maze.io.Loader;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.TerminalBuilder;

/**
 * Maze applicator.
 *
 * @author alnmlbch
 */
@UtilityClass
@Log4j2
public final class Application {

    public static void main(final String[] args) {
        try (var terminal = TerminalBuilder.terminal()) {
            final var io = IOHandler.of(
                LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build()
            );
            final var loader = Loader.of(io);
            if (!loader.getAsBoolean()) {
                log.warn("Maze wasn't generated");
            } else {
                log.info("Maze was generated, successfully loaded");
            }
        } catch (final IOException e) {
            log.error(e.getMessage());
        }
    }
}
