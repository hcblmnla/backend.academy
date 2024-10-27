package backend.academy.maze.io;

import backend.academy.maze.Maze;
import backend.academy.maze.exceptions.MazeInitException;
import backend.academy.maze.render.DynamicRenderer;
import backend.academy.maze.render.StaticRenderer;
import java.util.function.BooleanSupplier;
import lombok.RequiredArgsConstructor;

/**
 * Basic maze loader, which runs in application.
 *
 * @author alnmlbch
 */
@RequiredArgsConstructor(staticName = "of")
public final class Loader implements BooleanSupplier {

    private final Handler io;

    @Override
    public boolean getAsBoolean() {
        io.printLn();
        io.print("""
            Welcome to the maze generator, follow the instructions below!
               (for example, in colorful theme):
             * yellow cells are coins
             * pink cells are banks when you can lost your coins
             * start and finish are blue and red
            """);
        io.printLn();
        final var generator = io.getGenerator();
        Maze maze;
        try {
            maze = generator.generate(
                io.getGenerationType(),
                io.getParam("height"),
                io.getParam("width")
            );
        } catch (final MazeInitException e) {
            io.mazeCannotBeInitialized(e.getMessage());
            return false;
        }
        final var renderer = io.getRenderer();
        final var theme = io.getTheme();
        io.printImage(renderer.render(maze, theme).toString());
        if (io.getProcess() == Handler.Process.GENERATING) {
            io.print("Thanks for generating! Try to solve next time");
            io.printLn();
            return true;
        }
        final var solution = io.getSolver(maze).get();
        switch (renderer) {
            case StaticRenderer staticRenderer ->
                io.printImage(staticRenderer.render(maze, solution.coordinates(), theme));
            case DynamicRenderer dynamicRenderer -> {
                io.printLn();
                for (final var frame : dynamicRenderer.render(maze, solution.coordinates(), theme)) {
                    io.print(frame);
                    io.printLn();
                }
            }
            default -> {
            }
        }
        io.print("Thanks for playing!");
        io.print(" * you got %d coins and lost them in bank %d times", solution.coins(), solution.banks());
        io.print("""
             * green cells are solution of the maze
             * frames above if you choose dynamic renderer
            """);
        io.printLn();
        return true;
    }
}
