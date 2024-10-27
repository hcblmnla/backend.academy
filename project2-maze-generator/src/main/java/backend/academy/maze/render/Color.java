package backend.academy.maze.render;

import backend.academy.maze.Cell;
import backend.academy.util.PureFunctional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

/**
 * Colorful type of painter. Used unicode hacks.
 *
 * @author alnmlbch
 */
@PureFunctional
@RequiredArgsConstructor
@Getter
public enum Color {
    BLACK("\033[40m  \033[0m"),
    ORANGE("\033[41m  \033[0m"),
    GREEN("\033[42m  \033[0m"),
    YELLOW("\033[43m  \033[0m"),
    RIVER("\033[44m  \033[0m"),
    PINK("\033[45m  \033[0m"),
    BLUE("\033[46m  \033[0m"),
    GRAY("\033[47m  \033[0m");

    private final String code;

    /**
     * Mapper from {@link Cell}.
     */
    public static @NonNull Color of(final @NonNull Cell cell) {
        return switch (cell) {
            case WALL -> GRAY;
            case PATH -> BLACK;
            case SOLUTION -> GREEN;
            case START -> RIVER;
            case FINISH -> ORANGE;
            case COIN -> YELLOW;
            case BANK -> PINK;
        };
    }

    @Override
    public String toString() {
        return code;
    }
}
