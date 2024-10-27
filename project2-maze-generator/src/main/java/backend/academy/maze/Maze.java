package backend.academy.maze;

import backend.academy.maze.exceptions.EmptyGridException;
import backend.academy.maze.exceptions.EvenParamException;
import backend.academy.maze.exceptions.InvalidBordersException;
import backend.academy.maze.exceptions.InvalidInsideException;
import backend.academy.maze.exceptions.MarkerNotFoundException;
import backend.academy.maze.exceptions.MarkerOnAngleException;
import backend.academy.maze.exceptions.MazeInitException;
import backend.academy.maze.exceptions.MultipleMarkerException;
import backend.academy.util.ThrowIntBiConsumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

/**
 * Simple maze wrapper. It includes numerous validations of correctness.
 *
 * @author alnmlbch
 */
@Getter
public final class Maze {

    private static final String HEIGHT = "height";
    private static final String WIDTH = "width";
    private static final String NULL = "null";

    private final Cell[][] grid;
    private final Coordinate start;
    private final Coordinate finish;

    public Maze(final @NonNull Cell[][] grid) throws MazeInitException {
        if (grid.length - 2 < 1) {
            throw new EmptyGridException(HEIGHT);
        }
        if (grid[0].length - 2 < 1) {
            throw new EmptyGridException(WIDTH);
        }
        final int height = grid.length - 2;
        final int width = grid[0].length - 2;
        assertOddParam(height, width);
        final var settings = collectSettings(grid, height, width);
        if (settings.start == null) {
            throw new MarkerNotFoundException("start");
        }
        if (settings.finish == null) {
            throw new MarkerNotFoundException("finish");
        }
        this.grid = grid;
        this.start = settings.start;
        this.finish = settings.finish;
    }

    private static Settings collectSettings(
        final Cell[][] grid,
        final int height,
        final int width
    ) throws MazeInitException {
        final var settings = Settings.of(grid, height, width);
        for (int y = 0; y < height + 2; y++) {
            settings.accept(y, 0);
            settings.accept(y, width + 1);
        }
        for (int x = 0; x < width + 2; x++) {
            settings.accept(0, x);
            settings.accept(height + 1, x);
        }
        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                final var cell = grid[y][x];
                if (cell == null) {
                    throw new InvalidInsideException(NULL, y, x);
                }
                final var name = cell.log();
                switch (cell) {
                    case SOLUTION -> throw new InvalidInsideException(name, y, x);
                    case START, FINISH -> settings.reload(y, x, cell, name);
                    default -> {
                    }
                }
            }
        }
        return settings;
    }

    public static void assertOddParam(final int height, final int width) throws EvenParamException {
        if (height % 2 == 0) {
            throw new EvenParamException(HEIGHT, height);
        }
        if (width % 2 == 0) {
            throw new EvenParamException(WIDTH, width);
        }
    }

    public int getHeight() {
        return grid.length - 2;
    }

    public int getWidth() {
        return grid[0].length - 2;
    }

    /**
     * Simple maze settings temporary class wrapper.
     */
    @RequiredArgsConstructor(staticName = "of")
    private static class Settings implements ThrowIntBiConsumer<MazeInitException> {
        private final Cell[][] grid;
        private final int height;
        private final int width;
        private Coordinate start;
        private Coordinate finish;

        private void reload(
            final int y,
            final int x,
            final Cell marker,
            final String name
        ) throws MultipleMarkerException {
            if (marker == Cell.START) {
                if (start != null) {
                    throw new MultipleMarkerException(name, start.y(), start.x(), y, x);
                }
                start = new Coordinate(y, x);
            }
            if (marker == Cell.FINISH) {
                if (finish != null) {
                    throw new MultipleMarkerException(name, finish.y(), finish.x(), y, x);
                }
                finish = new Coordinate(y, x);
            }
        }

        @Override
        public void accept(final int y, final int x) throws MazeInitException {
            final var cell = grid[y][x];
            if (cell == null) {
                throw new InvalidBordersException(NULL, y, x);
            }
            final var name = cell.log();
            switch (cell) {
                case WALL -> {
                }
                case START, FINISH -> {
                    if ((y == 0 || y == height + 1) && (x == 0 || x == width + 1)) {
                        throw new MarkerOnAngleException(name, y, x);
                    }
                    reload(y, x, cell, name);
                }
                default -> throw new InvalidBordersException(name, y, x);
            }
        }
    }
}
