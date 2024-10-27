package backend.academy.maze.io;

import backend.academy.maze.Coordinate;
import backend.academy.maze.FailureInitTest;
import backend.academy.maze.Maze;
import backend.academy.maze.Settings;
import backend.academy.maze.algo.Generator;
import backend.academy.maze.algo.Solver;
import backend.academy.maze.render.Renderer;
import backend.academy.maze.render.Theme;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class LoaderTest {

    public static Stream<Settings> provideValidSettings() {
        return mapToSettings(Stream.of(
            new Coordinate(1, 1),
            new Coordinate(1, 3),
            new Coordinate(3, 1),
            new Coordinate(11, 7),
            new Coordinate(1, 101),
            new Coordinate(11, 13),
            new Coordinate(3, 101),
            new Coordinate(101, 3),
            new Coordinate(47, 33)
        ));
    }

    private static Stream<Settings> provideInvalidSettings() {
        return mapToSettings(FailureInitTest.provideEvenParams());
    }

    // in Java, we don't have for-comprehension :(
    public static Stream<Settings> mapToSettings(
        final Stream<Coordinate> coords
    ) {
        return coords
            .mapMulti((coord, consumer) -> Stream.of(
                    Handler.Process.values()
                ).forEach(handler -> Stream.of(
                        Generator.Algorithm.values()
                    ).forEach(genAlgo -> Stream.of(
                            Generator.Type.values()
                        ).forEach(genType -> Stream.of(
                                Renderer.Type.values()
                            ).forEach(rendererType -> Stream.of(
                                    Theme.values()
                                ).forEach(theme -> Stream.of(
                                        Solver.Algorithm.values()
                                    ).forEach(solverAlgo -> consumer.accept(new Settings(
                                            coord.y(),
                                            coord.x(),
                                            handler,
                                            genAlgo,
                                            genType,
                                            rendererType,
                                            theme,
                                            solverAlgo
                                        ))
                                    )
                                )
                            )
                        )
                    )
                )
            );
    }

    protected static Handler getHandler(final Settings settings) {
        return new Handler() {

            @Override
            public @NonNull Process getProcess() {
                return settings.process();
            }

            @Override
            public @NonNull Generator getGenerator() {
                return Generator.from(settings.genAlgo()).get();
            }

            @Override
            public Generator.@NonNull Type getGenerationType() {
                return settings.genType();
            }

            @Override
            public @NonNull Integer getParam(@NonNull String name) {
                return "height".equals(name) ? settings.height() : settings.width();
            }

            @Override
            public @NonNull Renderer<?, ?> getRenderer() {
                return Renderer.from(settings.rendererType());
            }

            @Override
            public @NonNull Theme getTheme() {
                return settings.theme();
            }

            @Override
            public @NonNull Solver getSolver(final @NonNull Maze maze) {
                return Solver.from(settings.solverAlgo()).apply(maze);
            }

            @Override
            public void mazeCannotBeInitialized(@NonNull String message) {
                // no implementation
            }

            @Override
            public void print(@NonNull String message, Object... args) {
                // no implementation
            }
        };
    }

    @ParameterizedTest
    @MethodSource("provideValidSettings")
    void validDataDoesNotThrowException(final Settings settings) {
        // Given
        final var loader = Loader.of(getHandler(settings));
        // Then
        assertThat(loader.getAsBoolean())
            .isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSettings")
    void invalidDataThrowsException(final Settings settings) {
        // Given
        final var loader = Loader.of(getHandler(settings));
        // Then
        assertThat(loader.getAsBoolean())
            .isFalse();
    }
}
