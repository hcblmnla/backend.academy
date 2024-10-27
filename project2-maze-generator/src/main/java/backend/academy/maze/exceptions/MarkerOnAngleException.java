package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public class MarkerOnAngleException extends MazeInitException {
    public MarkerOnAngleException(final @NonNull String name, final int y, final int x) {
        super("Marker %s cannot be on angle (%d, %d)".formatted(name, y, x));
    }
}
