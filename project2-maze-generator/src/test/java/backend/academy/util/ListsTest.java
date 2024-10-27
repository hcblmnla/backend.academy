package backend.academy.util;

import java.util.Collections;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ListsTest {

    @Test
    void nullFromEmptyList() {
        assertThat(Lists.getNullableRandomElement(
            Collections.<Object>emptyList(),
            new Random()
        )).isEqualTo(null);
    }

    @Test
    void thrownWithEmptyList() {
        assertThatThrownBy(() -> Lists.getRandomElement(
            Collections.emptyList(),
            new Random())
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void correctBehaviour(final int value) {
        // Given
        final var mocked = Mockito.mock(Random.class);
        // When
        Mockito
            .when(mocked.nextInt(Mockito.anyInt()))
            .thenReturn(value);
        // Then
        assertThat(Lists.getRandomElement(
            IntStream.range(0, 10)
                .boxed()
                .toList(),
            mocked
        )).isEqualTo(value);
        assertThat(Lists.getNullableRandomElement(
            IntStream.range(1, 12)
                .boxed()
                .toList(),
            mocked
        )).isEqualTo(value + 1);
    }
}
