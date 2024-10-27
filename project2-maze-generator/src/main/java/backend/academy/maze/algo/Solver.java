package backend.academy.maze.algo;

import backend.academy.maze.Maze;
import backend.academy.util.PureFunctional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * Maze solver contact.
 *
 * @author alnmlbch
 */
@PureFunctional
@FunctionalInterface
public interface Solver extends Supplier<Solution> {

    static @NonNull Function<Maze, Solver> from(final @NonNull Algorithm algorithm) {
        return switch (algorithm) {
            case DFS -> DfsSolver::new;
            case BFS -> BfsSolver::new;
        };
    }

    enum Algorithm {
        DFS, BFS
    }
}
