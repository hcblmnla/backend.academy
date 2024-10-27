package backend.academy.maze.algo;

import backend.academy.maze.Maze;
import backend.academy.maze.exceptions.MazeInitException;
import backend.academy.util.PureFunctional;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * Maze generator contract.
 *
 * @author alnmlbch
 */
@PureFunctional
public interface Generator {

    static @NonNull Supplier<Generator> from(final @NonNull Algorithm algorithm) {
        return switch (algorithm) {
            case PRIM -> PrimGenerator::new;
            case KRUSKAL -> KruskalGenerator::new;
        };
    }

    /**
     * Generate maze with classic position of start and finish.
     *
     * @param height maze height
     * @param width  maze width
     * @return generated maze.
     * @throws MazeInitException if constructor got invalid data.
     */
    @NonNull
    Maze classicGenerate(int height, int width) throws MazeInitException;

    /**
     * Generate maze with random position of start and finish.
     *
     * @param height maze height
     * @param width  maze width
     * @return generated maze.
     * @throws MazeInitException if constructor got invalid data.
     */
    @NonNull
    Maze randomGenerate(int height, int width) throws MazeInitException;

    default @NonNull Maze generate(final @NonNull Type type, final int height, final int width)
        throws MazeInitException {
        return switch (type) {
            case CLASSIC -> classicGenerate(height, width);
            case RANDOM -> randomGenerate(height, width);
        };
    }

    enum Algorithm {
        PRIM, KRUSKAL
    }

    enum Type {
        CLASSIC, RANDOM
    }
}
