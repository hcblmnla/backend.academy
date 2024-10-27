package backend.academy.maze.exceptions;

import org.jspecify.annotations.NonNull;

public class EvenParamException extends MazeInitException {
    public EvenParamException(final @NonNull String param, final int value) {
        super("The " + param + " cannot be even: " + value);
    }
}
