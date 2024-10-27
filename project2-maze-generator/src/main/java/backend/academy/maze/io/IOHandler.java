package backend.academy.maze.io;

import backend.academy.maze.Maze;
import backend.academy.maze.algo.Generator;
import backend.academy.maze.algo.Solver;
import backend.academy.maze.render.Renderer;
import backend.academy.maze.render.Theme;
import backend.academy.util.Functions;
import backend.academy.util.PureFunctional;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jline.reader.LineReader;
import org.jspecify.annotations.NonNull;

/**
 * Simple console handler, based on {@link LineReader}.
 *
 * @author alnmlbch
 */
@PureFunctional
@RequiredArgsConstructor(staticName = "of")
public class IOHandler implements Handler {

    private final LineReader reader;

    private static <T> Request<T> request(final String info, final Supplier<T> supplier) {
        return new Request<>(info, supplier);
    }

    private static String message(final String message) {
        return " > Enter " + message + ": ";
    }

    @Override
    public @NonNull Process getProcess() {
        return get(
            "desired process",
            request("only generating", () -> Process.GENERATING),
            request("generating and solving", () -> Process.ALL)
        );
    }

    @Override
    public @NonNull Generator getGenerator() {
        return Generator.from(get(
            "generator algorithm",
            request("Prim", () -> Generator.Algorithm.PRIM),
            request("Kruskal", () -> Generator.Algorithm.KRUSKAL)
        )).get();
    }

    @Override
    public Generator.@NonNull Type getGenerationType() {
        return get(
            "start and finish position",
            request("classic", () -> Generator.Type.CLASSIC),
            request("random", () -> Generator.Type.RANDOM)
        );
    }

    @Override
    public @NonNull Integer getParam(final @NonNull String name) {
        return tryToGet(() -> {
            try {
                final int found = Functions.compose(
                        Integer::parseInt,
                        reader::readLine,
                        IOHandler::message,
                        "%s (cannot be even and less than 1)"::formatted
                    )
                    .apply(name);
                if (found % 2 == 0 || found < 1) {
                    return null;
                }
                return found;
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    @Override
    public @NonNull Renderer<?, ?> getRenderer() {
        return Renderer.from(get(
            "renderer",
            request("static", () -> Renderer.Type.STATIC),
            request("dynamic", () -> Renderer.Type.DYNAMIC)
        ));
    }

    @Override
    public @NonNull Theme getTheme() {
        return get(
            "rendering theme",
            request("white-black", () -> Theme.WHITE_BLACK),
            request("colorful", () -> Theme.COLORFUL),
            request("telegram", () -> Theme.TELEGRAM)
        );
    }

    @Override
    public @NonNull Solver getSolver(final @NonNull Maze maze) {
        return Solver.from(get(
            "solver algorithm",
            request("DFS", () -> Solver.Algorithm.DFS),
            request("BFS", () -> Solver.Algorithm.BFS)
        )).apply(maze);
    }

    @Override
    public void print(final @NonNull String message, final @NonNull Object... args) {
        reader.printAbove(message.formatted(args));
    }

    @Override
    public void mazeCannotBeInitialized(final @NonNull String message) {
        reader.printAbove(" !> Maze initializing failed: " + message);
    }

    @SafeVarargs
    private <T> T get(final String message, final Request<T>... requests) {
        final var counter = new AtomicInteger();
        final var request = message("%s (%s)").formatted(
            message,
            Arrays.stream(requests)
                .map(Request::info)
                .map(info -> "[%d] %s".formatted(counter.incrementAndGet(), info))
                .collect(Collectors.joining(", "))
        );
        counter.lazySet(0);
        final Map<String, Supplier<T>> map = Arrays.stream(requests)
            .map(r -> request(Integer.toString(counter.incrementAndGet()), r.value))
            .map(Request::entry)
            .collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue
            ));
        return tryToGet(() -> map.getOrDefault(reader.readLine(request), () -> null).get());
    }

    private <T> T tryToGet(final Supplier<T> invoker) {
        while (true) {
            final var t = invoker.get();
            if (t != null) {
                return t;
            }
            reader.printAbove(" >> Invalid information, try again");
        }
    }

    private record Request<T>(String info, Supplier<T> value) {
        public Map.Entry<String, Supplier<T>> entry() {
            return Map.entry(info, value);
        }
    }
}
