package backend.academy.util.algo;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisjointSetUnionTest {

    private static DisjointSetUnion<Integer> templateDSU(final int bound) {
        return new DisjointSetUnion<>(
            IntStream.range(0, bound)
                .boxed()
                .toList()
        );
    }

    @Test
    void smallTest() {
        // Given
        final var dsu = templateDSU(10);
        // When-Then
        dsu.union(0, 1);
        assertTrue(dsu.inSameSet(1, 0));
        assertFalse(dsu.inSameSet(1, 2));
        dsu.union(0, 2); // 0-2
        assertTrue(dsu.inSameSet(1, 2));
        assertFalse(dsu.inSameSet(0, 3));
        dsu.union(4, 3);
        dsu.union(5, 4); // 0-2-5-4
        dsu.union(2, 5); // 0-2-5
        assertTrue(dsu.inSameSet(0, 4));
    }

    @Test
    void bigTest() {
        // Given
        final int amount = 10_000;
        final var dsu = templateDSU(amount);
        for (int i = 1; i < amount - 1; i++) {
            // When
            dsu.union(i - 1, i);
            // Then
            assertTrue(dsu.inSameSet(0, i));
            assertFalse(dsu.inSameSet(0, i + 1));
        }
    }
}
