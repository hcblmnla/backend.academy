package backend.academy.maze;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CoordinateTest {

    private static Stream<Arguments> provideArgs() {
        return Stream.of(
            Arguments.of(1, 1, 3, 3, 2, 2),
            Arguments.of(1, 2, 2, 1, 1, 1),
            Arguments.of(0, 0, 6, 7, 3, 3),
            Arguments.of(10, 1, 4, 7, 7, 4),
            Arguments.of(100, -100, -100, 100, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgs")
    void arithMeanTest(int y1, int x1, int y2, int x2, int y, int x) {
        // Given
        final var c1 = new Coordinate(y1, x1);
        final var c2 = new Coordinate(y2, x2);
        // Then
        assertThat(new Coordinate(c1.yWith(c2), c2.xWith(c1)))
            .isEqualTo(new Coordinate(y, x));
    }

    @ParameterizedTest
    @MethodSource("provideArgs")
    void redoubleTest(int y, int x) {
        assertThat(new Coordinate(y, x).redouble())
            .isEqualTo(new Coordinate(2 * y, 2 * x));
    }
}
