package backend.academy.maze.algo;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.util.Lists;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

/**
 * Prim algorithm for generating mazes.
 *
 * @author alnmlbch
 */
@NoArgsConstructor
public class PrimGenerator extends AbstractGenerator {

    private int getRandomParam(final int param) {
        return param == 1 ? 1 : random.nextInt(1, (param + 1) / 2) * 2 + 1;
    }

    @Override
    public void fillGrid(final @NonNull Cell[][] grid, final int height, final int width) {
        makeOddGrid(grid, height, width);
        final var used = new boolean[height + 2][width + 2];
        final Deque<Coordinate> last = new ArrayDeque<>();
        last.add(new Coordinate(getRandomParam(height), getRandomParam(width)));
        while (!last.isEmpty()) {
            final var cur = last.peek();
            final int y = cur.y();
            final int x = cur.x();
            used[y][x] = true;
            final List<Coordinate> available = new ArrayList<>();
            Direction.doubleDeltas()
                .forEachOrdered(coord -> {
                    final int dy = y + coord.y();
                    final int dx = x + coord.x();
                    if (dy >= 1 && dy <= height && dx >= 1 && dx <= width && !used[dy][dx]) {
                        available.add(new Coordinate(dy, dx));
                    }
                });
            if (available.isEmpty()) {
                last.remove();
            } else {
                final var next = Lists.getRandomElement(available, random);
                grid[cur.yWith(next)][cur.xWith(next)] = Cell.PATH;
                last.push(next);
            }
        }
    }
}
