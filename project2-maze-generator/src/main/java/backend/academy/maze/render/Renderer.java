package backend.academy.maze.render;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import backend.academy.util.PureFunctional;
import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * Simple maze renderer, which accepts custom painters.
 *
 * @param <U> the type of image of an unsolved maze
 * @param <S> the type of image of a solved maze
 * @author alnmlbch
 */
@PureFunctional
public interface Renderer<U, S> {

    static Renderer<String, ?> from(final @NonNull Type type) {
        return switch (type) {
            case STATIC -> new StaticRenderer();
            case DYNAMIC -> new DynamicRenderer();
        };
    }

    /**
     * Render only maze without solution.
     *
     * @param maze  unsolved maze
     * @param theme painter's theme
     * @return image of unsolved maze.
     */
    U render(@NonNull Maze maze, @NonNull Theme theme);

    /**
     * Render maze with solution.
     *
     * @param maze     solved maze
     * @param solution maze solution
     * @param theme    painter's theme
     * @return solved maze image/images.
     */
    S render(@NonNull Maze maze, @NonNull List<Coordinate> solution, @NonNull Theme theme);

    enum Type {
        STATIC, DYNAMIC
    }
}
