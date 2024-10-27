package backend.academy.maze.render;

import backend.academy.maze.Cell;
import backend.academy.maze.Maze;
import backend.academy.util.Functions;
import backend.academy.util.PureFunctional;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.jspecify.annotations.NonNull;

/**
 * Abstract renderer class for basic static variant.
 *
 * @param <S> the type of image of a solved maze
 */
@PureFunctional
// I can't find reason :(
@SuppressFBWarnings("FCCD_FIND_CLASS_CIRCULAR_DEPENDENCY")
public abstract class AbstractRenderer<S> implements Renderer<String, S> {

    private static Collector<String, StringBuilder, StringBuilder> getCollector() {
        return new Collector<>() {

            @Override
            public Supplier<StringBuilder> supplier() {
                return StringBuilder::new;
            }

            @Override
            public BiConsumer<StringBuilder, String> accumulator() {
                return StringBuilder::append;
            }

            @Override
            public BinaryOperator<StringBuilder> combiner() {
                return StringBuilder::append;
            }

            @Override
            public Function<StringBuilder, StringBuilder> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }

    private String renderRow(final @NonNull Cell[] row, final @NonNull Theme theme) {
        return "  %s".formatted(
            Arrays.stream(row)
                .map(theme.painter())
                .collect(getCollector())
                .append(System.lineSeparator())
                .toString()
        );
    }

    protected @NonNull String render(final @NonNull Cell[][] grid, final @NonNull Theme theme) {
        return Arrays.stream(grid)
            .map(row -> renderRow(row, theme))
            .collect(getCollector())
            .toString()
            .stripTrailing();
    }

    @Override
    public String render(final @NonNull Maze maze, final @NonNull Theme theme) {
        return Functions.compose(grid -> render(grid, theme), Maze::grid)
            .apply(maze);
    }
}
