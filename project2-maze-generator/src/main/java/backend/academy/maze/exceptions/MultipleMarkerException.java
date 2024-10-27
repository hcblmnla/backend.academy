package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public class MultipleMarkerException extends MazeInitException {

    public MultipleMarkerException(
        final @NonNull String name,
        final int y1,
        final int x1,
        final int y2,
        final int x2
    ) {
        super("Found multiple %s marker on (%d, %d) and (%d, %d)".formatted(name, y1, x1, y2, x2));
    }
}
