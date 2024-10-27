package backend.academy.util;

import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FunctionsTest {

    private static final Function<Integer, String> INC_TO_STRING = Functions.compose(
        Object::toString,
        x -> x + 1
    );

    private static final Function<Integer, Integer> INC_MUL_HASHCODE = Functions.compose(
        Object::hashCode,
        x -> x * 10,
        x -> x + 1
    );

    private static final Function<Integer, Integer> INC_MUL_TO_STRING_LENGTH = Functions.compose(
        String::length,
        Object::toString,
        x -> x * 10,
        x -> x + 1
    );

    private static Stream<Arguments> provideIncToString() {
        return Stream.of(
            Arguments.of(1, "2"),
            Arguments.of(2, "3"),
            Arguments.of(10, "11"),
            Arguments.of(999, "1000"),
            Arguments.of(-1001, "-1000"),
            Arguments.of(0, "1"),
            Arguments.of(-1, "0")
        );
    }

    private static Stream<Arguments> provideIncMulHashCode() {
        return Stream.of(
            Arguments.of(1, 20),
            Arguments.of(2, 30),
            Arguments.of(-1, 0),
            Arguments.of(-2, -10),
            Arguments.of(999, 10000),
            Arguments.of(0, 10)
        );
    }

    private static Stream<Arguments> provideIncMulToStringLength() {
        return Stream.of(
            Arguments.of(1, 2),
            Arguments.of(-1, 1),
            Arguments.of(0, 2),
            Arguments.of(7, 2),
            Arguments.of(999, 5),
            Arguments.of(-1000, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIncToString")
    void incToStringTest(final int n, final @NonNull String expected) {
        Assertions.assertThat(INC_TO_STRING.apply(n))
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideIncMulHashCode")
    void incMulHashCodeTest(final int n, final int expected) {
        Assertions.assertThat(INC_MUL_HASHCODE.apply(n))
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideIncMulToStringLength")
    void incMulToStringLengthTest(final int n, final int expected) {
        Assertions.assertThat(INC_MUL_TO_STRING_LENGTH.apply(n))
            .isEqualTo(expected);
    }
}
