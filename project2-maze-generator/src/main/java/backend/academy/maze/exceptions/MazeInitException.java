package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public abstract class MazeInitException extends Exception {
    protected MazeInitException(final @NonNull String message) {
        super(message);
    }
}
