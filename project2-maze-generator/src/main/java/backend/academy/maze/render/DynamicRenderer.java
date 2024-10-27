package backend.academy.maze.render;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NonNull;

/**
 * Dynamic renderer, which returns all frames.
 *
 * @author alnmlbch
 */
public class DynamicRenderer extends AbstractRenderer<Iterable<String>> {

    @Override
    public Iterable<String> render(
        final @NonNull Maze maze,
        final @NonNull List<Coordinate> solution,
        final @NonNull Theme theme
    ) {
        final var grid = maze.grid();
        AtomicInteger pos = new AtomicInteger();
        return () -> new Iterator<>() {

            @Override
            public boolean hasNext() {
                return pos.get() < solution.size();
            }

            @Override
            public String next() {
                final var coord = solution.get(pos.getAndIncrement());
                grid[coord.y()][coord.x()] = Cell.SOLUTION;
                return render(grid, theme);
            }
        };
    }
}
