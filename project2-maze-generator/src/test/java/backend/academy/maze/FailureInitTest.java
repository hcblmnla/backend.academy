package backend.academy.maze;

import backend.academy.maze.exceptions.EmptyGridException;
import backend.academy.maze.exceptions.EvenParamException;
import backend.academy.maze.exceptions.InvalidBordersException;
import backend.academy.maze.exceptions.InvalidInsideException;
import backend.academy.maze.exceptions.MarkerNotFoundException;
import backend.academy.maze.exceptions.MarkerOnAngleException;
import backend.academy.maze.exceptions.MazeInitException;
import backend.academy.maze.exceptions.MultipleMarkerException;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static backend.academy.maze.Cell.BANK;
import static backend.academy.maze.Cell.COIN;
import static backend.academy.maze.Cell.FINISH;
import static backend.academy.maze.Cell.PATH;
import static backend.academy.maze.Cell.SOLUTION;
import static backend.academy.maze.Cell.START;
import static backend.academy.maze.Cell.WALL;
import static backend.academy.maze.Ctor.ctorFromGrid;
import static backend.academy.maze.Ctor.ctorsFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * Big test-cases for validators correctness checking.
 *
 * @author alnmlbch
 */
@Log4j2
public class FailureInitTest {

    private static Stream<Arguments> provideArgs(
        final @NonNull Stream<Ctor> ctors,
        final @NonNull Class<? extends MazeInitException> exception
    ) {
        return ctors.map(ctor -> Arguments.of(ctor, exception));
    }

    public static Stream<Coordinate> provideEmptyGridParams() {
        return Stream.of(
            new Coordinate(2, 0),
            new Coordinate(0, 3),
            new Coordinate(0, 0),
            new Coordinate(3, -1),
            new Coordinate(-2, 6)
        );
    }

    public static Stream<Coordinate> provideEvenParams() {
        return Stream.of(
            new Coordinate(2, 1),
            new Coordinate(10, 3),
            new Coordinate(40, 20),
            new Coordinate(1, 6),
            new Coordinate(13, 2)
        );
    }

    private static Stream<Arguments> provideEmptyGrid() {
        return provideArgs(ctorsFrom(provideEmptyGridParams()), EmptyGridException.class);
    }

    private static Stream<Arguments> provideEvenParam() {
        return provideArgs(ctorsFrom(provideEvenParams()), EvenParamException.class);
    }

    private static Stream<Arguments> provideInvalidBorders() {
        return provideArgs(Stream.of(
            ctorFromGrid(
                1, 1,
                WALL, WALL, WALL,
                SOLUTION, PATH, WALL,
                WALL, START, FINISH
            ),
            ctorFromGrid(
                1, 1,
                WALL, WALL, PATH,
                WALL, START, FINISH,
                WALL, WALL, WALL
            ),
            ctorFromGrid(
                1, 3,
                WALL, WALL, WALL, WALL, WALL,
                WALL, WALL, WALL, START, FINISH,
                WALL, COIN, WALL, WALL, WALL
            ),
            ctorFromGrid(
                3, 1,
                WALL, START, WALL,
                WALL, WALL, WALL,
                WALL, PATH, WALL,
                FINISH, SOLUTION, WALL,
                null, WALL, WALL
            )
        ), InvalidBordersException.class);
    }

    private static Stream<Arguments> provideMarkerOnAngle() {
        return provideArgs(Stream.of(
            ctorFromGrid(
                1, 1,
                START, WALL, WALL,
                WALL, COIN, WALL,
                WALL, WALL, WALL
            ),
            ctorFromGrid(
                1, 1,
                WALL, WALL, WALL,
                WALL, START, WALL,
                FINISH, WALL, WALL
            ),
            ctorFromGrid(
                1, 1,
                WALL, WALL, START,
                WALL, BANK, WALL,
                WALL, WALL, FINISH
            )
        ), MarkerOnAngleException.class);
    }

    private static Stream<Arguments> provideMultipleMarker() {
        return provideArgs(Stream.of(
            ctorFromGrid(
                1, 1,
                WALL, WALL, WALL,
                START, PATH, FINISH,
                WALL, START, WALL
            ),
            ctorFromGrid(
                1, 3,
                WALL, WALL, START, WALL, WALL,
                FINISH, COIN, BANK, PATH, WALL,
                WALL, WALL, WALL, FINISH, WALL
            ),
            ctorFromGrid(
                1, 1,
                WALL, START, WALL,
                START, WALL, FINISH,
                WALL, FINISH, WALL
            )
        ), MultipleMarkerException.class);
    }

    private static Stream<Arguments> provideInvalidInside() {
        return provideArgs(Stream.of(
            ctorFromGrid(
                1, 1,
                WALL, START, WALL,
                FINISH, SOLUTION, WALL,
                WALL, WALL, WALL
            ),
            ctorFromGrid(
                3, 3,
                WALL, WALL, WALL, WALL, WALL,
                WALL, START, PATH, FINISH, WALL,
                WALL, BANK, PATH, COIN, WALL,
                WALL, null, FINISH, WALL, WALL,
                WALL, WALL, WALL, WALL, WALL
            ),
            ctorFromGrid(
                1, 3,
                WALL, WALL, WALL, WALL, WALL,
                START, SOLUTION, PATH, PATH, FINISH,
                WALL, WALL, WALL, WALL, WALL
            )
        ), InvalidInsideException.class);
    }

    private static Stream<Arguments> provideMarkerNotFound() {
        return provideArgs(Stream.of(
            ctorFromGrid(
                1, 1,
                WALL, WALL, WALL,
                WALL, PATH, WALL,
                WALL, WALL, WALL
            ),
            ctorFromGrid(
                3, 3,
                WALL, WALL, WALL, WALL, WALL,
                WALL, START, PATH, COIN, WALL,
                WALL, BANK, PATH, COIN, WALL,
                WALL, PATH, PATH, PATH, WALL,
                WALL, WALL, WALL, WALL, WALL
            ),
            ctorFromGrid(
                1, 3,
                WALL, WALL, WALL, WALL, WALL,
                WALL, PATH, PATH, PATH, FINISH,
                WALL, WALL, WALL, WALL, WALL
            )
        ), MarkerNotFoundException.class);
    }

    @ParameterizedTest
    @MethodSource({
        "provideEmptyGrid",
        "provideEvenParam",
        "provideInvalidBorders",
        "provideMarkerOnAngle",
        "provideMultipleMarker",
        "provideInvalidInside",
        "provideMarkerNotFound"
    })
    void withProvidedData(
        final @NonNull Ctor ctor,
        final @NonNull Class<? extends MazeInitException> exception
    ) {
        failWith(ctor, exception);
    }

    @TestFactory
    protected void failWith(
        final @NonNull Ctor ctor,
        final @NonNull Class<? extends MazeInitException> exception
    ) {
        // Given
        final var thrown = catchThrowable(ctor::call);
        // Then
        assertThat(thrown)
            .isInstanceOf(exception);
        log.info("{}\n > {}", exception.getSimpleName(), thrown.getMessage());
    }

    @SneakyThrows
    @TestFactory
    protected void noFailWith(final int height, final int width, final @NonNull Ctor ctor) {
        Assertions.assertDoesNotThrow(ctor::call);
        // Given
        final var maze = ctor.call();
        // Then
        Assertions.assertEquals(maze.getHeight(), height);
        Assertions.assertEquals(maze.getWidth(), width);
    }
}
