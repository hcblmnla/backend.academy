package backend.academy.maze.algo;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import backend.academy.maze.exceptions.EvenParamException;
import backend.academy.maze.exceptions.MazeInitException;
import backend.academy.util.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import org.jspecify.annotations.NonNull;

/**
 * Abstract generator class.
 *
 * @author alnmlbch
 */
public abstract class AbstractGenerator implements Generator {

    protected Random random = new SecureRandom();

    protected abstract void fillGrid(@NonNull Cell[][] grid, int height, int width);

    private boolean nextRarelyBoolean(final int height, final int width) {
        return random.nextInt(Integer.min(height, width) * 2) == 0;
    }

    private void forAll(final int height, final int width, final BiConsumer<Integer, Integer> action) {
        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                action.accept(y, x);
            }
        }
    }

    private Cell[][] grid(final int height, final int width) throws EvenParamException {
        Maze.assertOddParam(height, width);
        final var grid = new Cell[height + 2][width + 2];
        for (final var row : grid) {
            Arrays.fill(row, Cell.WALL);
        }
        fillGrid(grid, height, width);
        forAll(height, width, (y, x) -> {
            if (grid[y][x] == Cell.PATH && nextRarelyBoolean(height, width)) {
                grid[y][x] = random.nextBoolean() ? Cell.COIN : Cell.BANK;
            }
        });
        return grid;
    }

    @SuppressFBWarnings("CLI_CONSTANT_LIST_INDEX")
    private void putClassicMarkers(final Cell[][] grid, final int height, final int width) {
        grid[1][0] = Cell.START;
        grid[height][width + 1] = Cell.FINISH;
    }

    @Override
    public @NonNull Maze classicGenerate(final int height, final int width) throws MazeInitException {
        final var grid = grid(height, width);
        putClassicMarkers(grid, height, width);
        return new Maze(grid);
    }

    @Override
    public @NonNull Maze randomGenerate(int height, int width) throws MazeInitException {
        final var grid = grid(height, width);
        final List<Coordinate> available = new ArrayList<>();
        forAll(height, width, (y, x) -> {
            if (grid[y][x] == Cell.PATH) {
                available.add(new Coordinate(y, x));
            }
        });
        if (available.size() < 2) {
            putClassicMarkers(grid, height, width);
        } else {
            final var start = Lists.getRandomElement(available, random);
            Coordinate finish;
            do {
                finish = Lists.getRandomElement(available, random);
            } while (start.equals(finish));
            grid[start.y()][start.x()] = Cell.START;
            grid[finish.y()][finish.x()] = Cell.FINISH;
        }
        return new Maze(grid);
    }

    protected void makeOddGrid(final @NonNull Cell[][] grid, final int height, final int width) {
        for (int y = 1; y <= height; y += 2) {
            for (int x = 1; x <= width; x += 2) {
                grid[y][x] = Cell.PATH;
            }
        }
    }
}
