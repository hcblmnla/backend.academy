package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public class EmptyGridException extends MazeInitException {
    public EmptyGridException(final @NonNull String param) {
        super("The grid is empty, " + param + " should be positive");
    }
}
