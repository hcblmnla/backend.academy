package backend.academy.maze.algo;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.util.algo.DisjointSetUnion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

/**
 * Kruskal algorithm for generating mazes.
 *
 * @author alnmlbch
 */
@NoArgsConstructor
public class KruskalGenerator extends AbstractGenerator {

    private void addEdge(final List<Edge> edges, final int y1, final int x1, final int y2, final int x2) {
        edges.add(new Edge(
            new Coordinate(y1, x1),
            new Coordinate(y2, x2),
            random.nextInt()
        ));
    }

    @Override
    public void fillGrid(final @NonNull Cell[][] grid, final int height, final int width) {
        makeOddGrid(grid, height, width);
        final int capacity = height * width;
        final List<Coordinate> vertices = new ArrayList<>(capacity);
        final List<Edge> edges = new ArrayList<>(capacity);
        for (int x = 1; x < width; x += 2) {
            for (int y = 1; y < height; y += 2) {
                vertices.add(new Coordinate(y, x));
                addEdge(edges, y, x, y + 2, x);
                addEdge(edges, y, x, y, x + 2);
            }
            addEdge(edges, height, x, height, x + 2);
        }
        for (int y = 1; y < height; y += 2) {
            vertices.add(new Coordinate(y, width));
            addEdge(edges, y, width, y + 2, width);
        }
        for (int x = 1; x <= width; x += 2) {
            vertices.add(new Coordinate(height, x));
        }
        vertices.add(new Coordinate(height, width));
        edges.sort(Comparator.comparingInt(Edge::weight));
        final DisjointSetUnion<Coordinate> dsu = new DisjointSetUnion<>(vertices);
        for (final var e : edges) {
            final var v = e.from();
            final var u = e.to();
            if (!dsu.inSameSet(v, u)) {
                dsu.union(v, u);
                grid[v.yWith(u)][v.xWith(u)] = Cell.PATH;
            }
        }
    }

    private record Edge(Coordinate from, Coordinate to, int weight) {
    }
}
