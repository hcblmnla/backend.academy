package backend.academy.maze.algo;

import backend.academy.maze.Maze;
import org.jspecify.annotations.NonNull;

/**
 * Classic DFS algorithm implementation.
 *
 * @author alnmlbch
 */
public class DfsSolver extends AbstractSearchSolver {

    public DfsSolver(final @NonNull Maze maze) {
        super(maze);
    }

    private void dfs(final int y, final int x) {
        used[y][x] = true;
        handle(y, x, this::dfs);
    }

    @Override
    public void solve() {
        dfs(start.y(), start.x());
    }
}
