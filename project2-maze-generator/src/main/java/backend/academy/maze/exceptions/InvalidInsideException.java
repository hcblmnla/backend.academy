package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public class InvalidInsideException extends InvalidDetailException {
    public InvalidInsideException(final @NonNull String name, final int y, final int x) {
        super("inside of the maze", name, y, x);
    }
}
