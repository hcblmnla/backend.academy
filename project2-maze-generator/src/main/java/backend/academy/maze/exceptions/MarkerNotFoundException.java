package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public class MarkerNotFoundException extends MazeInitException {
    public MarkerNotFoundException(final @NonNull String name) {
        super("The marker " + name + " was not found");
    }
}
