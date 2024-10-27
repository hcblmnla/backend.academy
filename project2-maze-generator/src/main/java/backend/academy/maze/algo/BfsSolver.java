package backend.academy.maze.algo;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.ArrayDeque;
import java.util.Queue;
import org.jspecify.annotations.NonNull;

/**
 * Classic BFS algorithm implementation.
 *
 * @author alnmlbch
 */
public class BfsSolver extends AbstractSearchSolver {

    public BfsSolver(final @NonNull Maze maze) {
        super(maze);
    }

    @Override
    public void solve() {
        final Queue<Coordinate> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            var current = queue.remove();
            handle(current.y(), current.x(), (dy, dx) -> {
                used[dy][dx] = true;
                queue.add(new Coordinate(dy, dx));
            });
        }
    }
}
