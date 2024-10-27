package backend.academy.maze;

import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static backend.academy.maze.Cell.BANK;
import static backend.academy.maze.Cell.COIN;
import static backend.academy.maze.Cell.FINISH;
import static backend.academy.maze.Cell.PATH;
import static backend.academy.maze.Cell.START;
import static backend.academy.maze.Cell.WALL;
import static backend.academy.maze.Ctor.ctorFromGrid;

public class MazeInitTest extends FailureInitTest {

    private static Stream<Arguments> provideNoThrow() {
        return Stream.of(
            Arguments.of(
                1, 1,
                ctorFromGrid(
                    1, 1,
                    WALL, WALL, WALL,
                    START, PATH, FINISH,
                    WALL, WALL, WALL
                )
            ),
            Arguments.of(
                3, 3,
                ctorFromGrid(
                    3, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    WALL, START, PATH, COIN, WALL,
                    WALL, FINISH, WALL, BANK, WALL,
                    WALL, PATH, WALL, PATH, WALL,
                    WALL, WALL, WALL, WALL, WALL
                )
            ),
            Arguments.of(
                1, 1,
                ctorFromGrid(
                    1, 1,
                    WALL, WALL, WALL,
                    START, BANK, FINISH,
                    WALL, WALL, WALL
                )
            )
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideNoThrow")
    void noThrow(final int height, final int width, final @NonNull Ctor ctor) {
        noFailWith(height, width, ctor);
    }
}
