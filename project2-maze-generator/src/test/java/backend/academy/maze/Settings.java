package backend.academy.maze;

import backend.academy.maze.algo.Generator;
import backend.academy.maze.algo.Solver;
import backend.academy.maze.io.Handler;
import backend.academy.maze.render.Renderer;
import backend.academy.maze.render.Theme;
import org.jspecify.annotations.NonNull;

/**
 * Settings for simpler testing.
 *
 * @author alnmlbch
 */
public record Settings(
    int height,
    int width,
    Handler.@NonNull Process process,
    Generator.@NonNull Algorithm genAlgo,
    Generator.@NonNull Type genType,
    Renderer.@NonNull Type rendererType,
    @NonNull Theme theme,
    Solver.@NonNull Algorithm solverAlgo
) {
}
