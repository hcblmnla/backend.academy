package backend.academy.maze.render;

import backend.academy.maze.Cell;
import backend.academy.util.PureFunctional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@PureFunctional
@RequiredArgsConstructor
@Getter
public enum Emoji {
    BROWN_BLOCK("\uD83D\uDFEB"),
    RED_BLOCK("\uD83D\uDFE5"),
    GREEN_BLOCK("\uD83D\uDFE9"),
    YELLOW_BLOCK("\uD83D\uDFE8"),
    PINK_BLOCK("\uD83D\uDFEA"),
    BLUE_LOCK("\uD83D\uDFE6"),
    ORANGE_BLOCK("\uD83D\uDFE7");

    private final String emoji;

    /**
     * Mapper from {@link Cell}.
     */
    public static @NonNull Emoji of(final @NonNull Cell cell) {
        return switch (cell) {
            case WALL -> BROWN_BLOCK;
            case PATH -> ORANGE_BLOCK;
            case SOLUTION -> GREEN_BLOCK;
            case START -> BLUE_LOCK;
            case FINISH -> RED_BLOCK;
            case COIN -> YELLOW_BLOCK;
            case BANK -> PINK_BLOCK;
        };
    }

    @Override
    public String toString() {
        return emoji;
    }
}
