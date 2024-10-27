package backend.academy.util;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ThrowIntBiConsumerTest {

    private static final ThrowIntBiConsumer<ArithmeticException> DIVISOR = (_, r) -> {
        if (r == 0) {
            throw new ArithmeticException("Division by zero");
        }
    };

    private static Stream<Arguments> provideNoThrowArguments() {
        return Stream.of(
            Arguments.of(1, 2),
            Arguments.of(10, 3),
            Arguments.of(0, 5),
            Arguments.of(0, -20),
            Arguments.of(3, -6),
            Arguments.of(-100, -25)
        );
    }

    private static Stream<Arguments> provideThrowArguments() {
        return Stream.of(
            Arguments.of(1, 0),
            Arguments.of(20, 0),
            Arguments.of(-3, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNoThrowArguments")
    void noThrowTest(final int left, final int right) {
        Assertions.assertDoesNotThrow(() -> DIVISOR.accept(left, right));
    }

    @ParameterizedTest
    @MethodSource("provideThrowArguments")
    void throwTest(final int left, final int right) {
        Assertions.assertThrows(ArithmeticException.class, () -> DIVISOR.accept(left, right));
    }
}
