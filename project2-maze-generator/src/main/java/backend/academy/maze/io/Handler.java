package backend.academy.maze.io;

import backend.academy.maze.Maze;
import backend.academy.maze.algo.Generator;
import backend.academy.maze.algo.Solver;
import backend.academy.maze.render.Renderer;
import backend.academy.maze.render.Theme;
import backend.academy.util.PureFunctional;
import org.jspecify.annotations.NonNull;

/**
 * Input-output handler contract. Provider of all settings.
 *
 * @author alnmlbch
 */
@PureFunctional
public interface Handler {

    /**
     * Process.
     *
     * @see Process
     */
    @NonNull Process getProcess();

    /**
     * Generator.
     *
     * @see Generator
     */
    @NonNull Generator getGenerator();

    /**
     * Generator type.
     *
     * @see Generator.Type
     */
    Generator.@NonNull Type getGenerationType();

    /**
     * Height or width.
     */
    @NonNull Integer getParam(@NonNull String name);

    /**
     * Renderer.
     *
     * @see Renderer
     */
    @NonNull Renderer<?, ?> getRenderer();

    /**
     * Renderer theme.
     *
     * @see Theme
     */
    @NonNull Theme getTheme();

    /**
     * Solver.
     *
     * @see Solver
     */
    @NonNull Solver getSolver(@NonNull Maze maze);

    void mazeCannotBeInitialized(@NonNull String message);

    void print(@NonNull String message, Object... args);

    default void printImage(final @NonNull String image) {
        print("%n%s%n%n".formatted(image));
    }

    default void printLn() {
        print(System.lineSeparator());
    }

    /**
     * Application process.
     */
    enum Process {
        GENERATING, ALL
    }
}
