package backend.academy.maze.render;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * Static renderer, which returns just a final frame.
 *
 * @author alnmlbch
 */
public class StaticRenderer extends AbstractRenderer<String> {

    @Override
    public String render(
        final @NonNull Maze maze,
        final @NonNull List<Coordinate> solution,
        final @NonNull Theme theme
    ) {
        final var grid = maze.grid();
        solution.forEach(coord -> grid[coord.y()][coord.x()] = Cell.SOLUTION);
        return render(grid, theme);
    }
}
