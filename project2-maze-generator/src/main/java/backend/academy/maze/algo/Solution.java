package backend.academy.maze.algo;

import backend.academy.maze.Coordinate;
import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * Simple solution wrapper.
 *
 * @param coordinates solution path
 * @param coins       picked money counter
 * @param banks       lost money counter
 * @author alnmlbch
 */
public record Solution(@NonNull List<Coordinate> coordinates, int coins, int banks) {

    public static Solution of(
        final int coins,
        final int banks,
        final @NonNull Coordinate... coordinates
    ) {
        return new Solution(List.of(coordinates), coins, banks);
    }
}
