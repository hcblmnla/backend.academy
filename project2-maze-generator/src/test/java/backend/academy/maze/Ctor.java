package backend.academy.maze;

import java.util.concurrent.Callable;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Some useful functions.
 *
 * @author alnmlbch
 */
@FunctionalInterface
public interface Ctor extends Callable<Maze> {

    static Stream<Ctor> ctorsFrom(final @NonNull Stream<Coordinate> coordinates) {
        return coordinates.map(coord -> () -> new Maze(new Cell[coord.y() + 2][coord.x() + 2]));
    }

    static Ctor ctorFromGrid(
        final int height,
        final int width,
        final @Nullable Cell... cells
    ) {
        final var grid = new Cell[height + 2][width + 2];
        for (int y = 0; y < height + 2; y++) {
            System.arraycopy(cells, y * (width + 2), grid[y], 0, width + 2);
        }
        return () -> new Maze(grid);
    }
}
