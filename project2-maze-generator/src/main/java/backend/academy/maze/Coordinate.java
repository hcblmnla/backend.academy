package backend.academy.maze;

import backend.academy.util.PureFunctional;
import org.jspecify.annotations.NonNull;

/**
 * Simple maze coordinate. Can be used for the params.
 *
 * @param y height coordinate
 * @param x width coordinate
 * @author alnmlbch
 */
@PureFunctional
public record Coordinate(int y, int x) {

    /**
     * Return arith mean of {@code y}.
     */
    public int yWith(final @NonNull Coordinate other) {
        return (y + other.y) / 2;
    }

    /**
     * Return arith mean of {@code x}.
     */
    public int xWith(final @NonNull Coordinate other) {
        return (x + other.x) / 2;
    }

    public Coordinate redouble() {
        return new Coordinate(2 * y, 2 * x);
    }
}
