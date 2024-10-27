package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public class InvalidBordersException extends InvalidDetailException {
    public InvalidBordersException(final @NonNull String name, final int y, final int x) {
        super("borders", name, y, x);
    }
}
