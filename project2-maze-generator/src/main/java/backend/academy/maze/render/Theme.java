package backend.academy.maze.render;

import backend.academy.maze.Cell;
import backend.academy.util.Functions;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;

/**
 * Rendering theme.
 *
 * @author alnmlbch
 */
public enum Theme {
    WHITE_BLACK, COLORFUL, TELEGRAM;

    public @NonNull Function<Cell, String> painter() {
        return Functions.compose(Object::toString, switch (this) {
            case WHITE_BLACK -> Symbol::of;
            case COLORFUL -> Color::of;
            case TELEGRAM -> Emoji::of;
        });
    }
}
