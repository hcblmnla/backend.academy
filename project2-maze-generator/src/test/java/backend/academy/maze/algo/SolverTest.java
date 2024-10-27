package backend.academy.maze.algo;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Ctor;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.SneakyThrows;
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
import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {

    private static Coordinate c(final int height, final int width) {
        return new Coordinate(height, width);
    }

    @SneakyThrows
    private static Stream<Arguments> solvable() {
        return Stream.of(
            Arguments.of(
                ctorFromGrid(
                    1, 1,
                    WALL, WALL, WALL,
                    START, PATH, FINISH,
                    WALL, WALL, WALL
                ),
                Solution.of(
                    0, 0, c(1, 1)
                ),
                Solver.Algorithm.BFS
            ),
            Arguments.of(
                ctorFromGrid(
                    3, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    WALL, START, PATH, COIN, WALL,
                    WALL, FINISH, WALL, BANK, WALL,
                    WALL, PATH, WALL, PATH, WALL,
                    WALL, WALL, WALL, WALL, WALL
                ),
                Solution.of(
                    0, 0
                ),
                Solver.Algorithm.DFS
            ),
            Arguments.of(
                ctorFromGrid(
                    3, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    WALL, START, PATH, COIN, WALL,
                    WALL, WALL, WALL, BANK, WALL,
                    WALL, PATH, WALL, FINISH, WALL,
                    WALL, WALL, WALL, WALL, WALL
                ),
                Solution.of(
                    1, 1, c(1, 2), c(1, 3), c(2, 3)
                ),
                Solver.Algorithm.BFS
            ),
            Arguments.of(
                ctorFromGrid(
                    1, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    START, COIN, COIN, COIN, FINISH,
                    WALL, WALL, WALL, WALL, WALL
                ),
                Solution.of(
                    3, 0, c(1, 1), c(1, 2), c(1, 3)
                ),
                Solver.Algorithm.BFS
            ),
            Arguments.of(
                ctorFromGrid(
                    5, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    START, COIN, COIN, WALL, WALL,
                    WALL, WALL, COIN, WALL, WALL,
                    WALL, WALL, COIN, WALL, WALL,
                    WALL, WALL, COIN, WALL, WALL,
                    WALL, WALL, COIN, BANK, FINISH,
                    WALL, WALL, WALL, WALL, WALL
                ),
                Solution.of(
                    6, 1,
                    c(1, 1), c(1, 2), c(2, 2), c(3, 2),
                    c(4, 2), c(5, 2), c(5, 3)
                ),
                Solver.Algorithm.DFS
            )
        );
    }

    @SneakyThrows
    private static Stream<Arguments> unsolvable() {
        return Stream.of(
                ctorFromGrid(
                    1, 1,
                    WALL, WALL, WALL,
                    START, WALL, FINISH,
                    WALL, WALL, WALL
                ),
                ctorFromGrid(
                    1, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    START, COIN, COIN, WALL, FINISH,
                    WALL, WALL, WALL, WALL, WALL
                ),
                ctorFromGrid(
                    5, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    START, COIN, WALL, COIN, WALL,
                    WALL, WALL, COIN, WALL, WALL,
                    WALL, WALL, COIN, WALL, WALL,
                    WALL, WALL, COIN, WALL, WALL,
                    WALL, WALL, COIN, BANK, FINISH,
                    WALL, WALL, WALL, WALL, WALL
                ),
                ctorFromGrid(
                    3, 3,
                    WALL, WALL, WALL, WALL, WALL,
                    WALL, START, PATH, COIN, WALL,
                    WALL, WALL, WALL, BANK, WALL,
                    WALL, PATH, WALL, PATH, WALL,
                    WALL, WALL, FINISH, WALL, WALL
                )
            )
            .map(c -> Arrays.stream(Solver.Algorithm.values())
                .map(algorithm -> Arguments.of(c, algorithm))
                .toList())
            .flatMap(Collection::stream);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("solvable")
    void checkSolvable(
        final Ctor ctor,
        final Solution solution,
        final Solver.Algorithm algorithm
    ) {
        assertThat(
            Solver
                .from(algorithm)
                .apply(ctor.call())
                .get()
        ).isEqualTo(solution);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("unsolvable")
    void checkUnsolvable(final Ctor ctor, final Solver.Algorithm algorithm) {
        assertThat(Solver
            .from(algorithm)
            .apply(ctor.call())
            .get()
        ).isNull();
    }
}
