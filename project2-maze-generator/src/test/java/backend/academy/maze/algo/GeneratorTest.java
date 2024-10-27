package backend.academy.maze.algo;

import backend.academy.maze.FailureInitTest;
import backend.academy.maze.Settings;
import backend.academy.maze.io.LoaderTest;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class GeneratorTest extends FailureInitTest {

    private static Stream<Settings> provideValidSettings() {
        return LoaderTest.provideValidSettings();
    }

    @ParameterizedTest
    @MethodSource("provideValidSettings")
    void generateNoThrow(final Settings settings) {
        Assertions.assertDoesNotThrow(
            () -> Generator
                .from(settings.genAlgo())
                .get()
                .generate(
                    settings.genType(),
                    settings.height(),
                    settings.width()
                )
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideValidSettings")
    void alwaysSolvable(final Settings settings) {
        try {
            assertThat(Solver
                .from(settings.solverAlgo())
                .apply(
                    Generator
                        .from(settings.genAlgo())
                        .get()
                        .generate(
                            settings.genType(),
                            settings.height(),
                            settings.width()
                        )
                )
                .get()
            )
                .isNotNull();
        } catch (StackOverflowError e) {
            assertThat(settings.solverAlgo())
                .withFailMessage("Only DFS can throw StackOverflowError")
                .isEqualTo(Solver.Algorithm.DFS);
        }
    }
}
