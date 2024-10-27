package backend.academy.maze.render;

import backend.academy.maze.Cell;
import backend.academy.util.PureFunctional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

/**
 * White-black type of painter.
 *
 * @author alnmlbch
 */
@PureFunctional
@RequiredArgsConstructor
@Getter
public enum Symbol {
    HASHTAG("#"),
    STAR("*"),
    PLUS("+"),
    QUESTION("?"),
    POINT("⋅"),
    S("S"),
    F("F");

    private final String symbol;

    /**
     * Mapper from {@link Cell}.
     */
    public static @NonNull Symbol of(final @NonNull Cell cell) {
        return switch (cell) {
            case WALL -> HASHTAG;
            case PATH -> POINT;
            case SOLUTION -> PLUS;
            case START -> S;
            case FINISH -> F;
            case COIN -> STAR;
            case BANK -> QUESTION;
        };
    }

    @Override
    public String toString() {
        return symbol;
    }
}
