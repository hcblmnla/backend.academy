package backend.academy.maze.algo;

import backend.academy.maze.Coordinate;
import backend.academy.util.PureFunctional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

/**
 * Possible generating directions.
 *
 * @author alnmlbch
 */
@PureFunctional
@RequiredArgsConstructor
public enum Direction {
    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    private final int dy;
    private final int dx;

    public static Stream<Coordinate> doubleDeltas() {
        return singleDeltas()
            .map(Coordinate::redouble);
    }

    public static Stream<Coordinate> singleDeltas() {
        return Stream.of(values())
            .map(dir -> new Coordinate(dir.dx, dir.dy));
    }
}
