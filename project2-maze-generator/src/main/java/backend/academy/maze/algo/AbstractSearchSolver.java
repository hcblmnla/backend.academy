package backend.academy.maze.algo;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.jspecify.annotations.NonNull;

/**
 * Abstract solver class for search algorithms like {@link DfsSolver} or {@link BfsSolver}.
 *
 * @author alnmlbch
 */
public abstract class AbstractSearchSolver extends AbstractSolver {

    protected final Coordinate start;
    protected final boolean[][] used;
    private final Cell[][] grid;
    private final int width;
    private final Coordinate finish;
    private final Coordinate[][] parent;

    protected AbstractSearchSolver(final @NonNull Maze maze) {
        grid = maze.grid();
        width = maze.getWidth();
        start = maze.start();
        finish = maze.finish();
        final int height = maze.getHeight();
        parent = new Coordinate[height + 2][width + 2];
        used = new boolean[height + 2][width + 2];
    }

    protected abstract void solve();

    @Override
    public Solution get() {
        solve();
        return getSolution();
    }

    private boolean isValid(final boolean[][] used, final int dy, final int dx) {
        return dx >= 0 && dx <= width + 1 && !used[dy][dx] && GOOD_CELLS.contains(grid[dy][dx]);
    }

    protected void handle(final int y, final int x, final @NonNull BiConsumer<Integer, Integer> action) {
        Direction.singleDeltas()
            .forEachOrdered(delta -> {
                int dy = y + delta.y();
                int dx = x + delta.x();
                if (isValid(used, dy, dx)) {
                    parent[dy][dx] = new Coordinate(y, x);
                    action.accept(dy, dx);
                }
            });
    }

    private Solution getSolution() {
        final List<Coordinate> solution = new ArrayList<>();
        Coordinate current = finish;
        int coins = 0;
        int banks = 0;
        while (true) {
            solution.add(current);
            final int y = current.y();
            final int x = current.x();
            current = parent[y][x];
            switch (grid[y][x]) {
                case COIN -> coins++;
                case BANK -> banks++;
                default -> {
                }
            }
            if (current == null) {
                return null;
            }
            if (current.equals(start)) {
                return new Solution(
                    solution
                        .subList(1, solution.size())
                        .reversed(),
                    coins,
                    banks
                );
            }
        }
    }
}
