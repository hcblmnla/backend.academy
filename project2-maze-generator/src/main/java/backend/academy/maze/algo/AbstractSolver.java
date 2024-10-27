package backend.academy.maze.algo;

import backend.academy.maze.Cell;
import java.util.Set;

/**
 * Abstract solver class. But for all solvers. For search solvers we have {@link AbstractSearchSolver}.
 *
 * @author alnmlbch
 */
public abstract class AbstractSolver implements Solver {

    protected static final Set<Cell> GOOD_CELLS = Set.of(
        Cell.PATH,
        Cell.COIN,
        Cell.BANK,
        Cell.FINISH
    );
}
