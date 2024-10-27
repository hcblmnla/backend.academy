package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public abstract class InvalidDetailException extends MazeInitException {

    protected InvalidDetailException(
        final @NonNull String detail,
        final @NonNull String name,
        final int y,
        final int x
    ) {
        super("The %s should not contain %s at (%d, %d)".formatted(detail, name, y, x));
    }
}
