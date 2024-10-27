package backend.academy.maze.render;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static backend.academy.maze.Cell.BANK;
import static backend.academy.maze.Cell.COIN;
import static backend.academy.maze.Cell.FINISH;
import static backend.academy.maze.Cell.PATH;
import static backend.academy.maze.Cell.START;
import static backend.academy.maze.Cell.WALL;
import static backend.academy.maze.Ctor.ctorFromGrid;
import static org.assertj.core.api.Assertions.assertThat;

public class RendererTest {

    @SneakyThrows
    private static Maze getMaze() {
        return ctorFromGrid(
            3, 3,
            WALL, WALL, START, WALL, WALL,
            WALL, COIN, PATH, WALL, WALL,
            WALL, WALL, PATH, BANK, WALL,
            WALL, PATH, COIN, PATH, WALL,
            WALL, FINISH, WALL, WALL, WALL
        ).call();
    }

    private static List<Coordinate> getCoordinates() {
        return List.of(
            new Coordinate(1, 2),
            new Coordinate(2, 2),
            new Coordinate(3, 2),
            new Coordinate(3, 1)
        );
    }

    @ParameterizedTest
    @EnumSource(Renderer.Type.class)
    void correctnessSymbols(final Renderer.Type type) {
        // Given
        final var renderer = Renderer.from(type);
        // Then
        assertThat(renderer.render(getMaze(), Theme.WHITE_BLACK)).isEqualTo("""
              ##S##
              #*⋅##
              ##⋅?#
              #⋅*⋅#
              #F###
            """.stripTrailing()
        );
        assertThat(renderer.render(getMaze(), Theme.COLORFUL))
            .isNotEmpty();
    }

    @Test
    void correctnessStaticSolution() {
        // Given
        final var staticRenderer = new StaticRenderer();
        // Then
        assertThat(staticRenderer.render(
            getMaze(),
            getCoordinates(),
            Theme.WHITE_BLACK
        ))
            .isEqualTo("""
                  ##S##
                  #*+##
                  ##+?#
                  #++⋅#
                  #F###
                """.stripTrailing());
    }

    @Test
    void correctnessDynamicSolution() {
        // Given
        final var expectedImages = List.of(
            """
                  ##S##
                  #*+##
                  ##⋅?#
                  #⋅*⋅#
                  #F###
                """,
            """
                  ##S##
                  #*+##
                  ##+?#
                  #⋅*⋅#
                  #F###
                """,
            """
                  ##S##
                  #*+##
                  ##+?#
                  #⋅+⋅#
                  #F###
                """,
            """
                  ##S##
                  #*+##
                  ##+?#
                  #++⋅#
                  #F###
                """
        );
        final var dynamicRenderer = new DynamicRenderer();
        // When
        int index = 0;
        for (final var image : dynamicRenderer.render(
            getMaze(),
            getCoordinates(),
            Theme.WHITE_BLACK
        )) {
            // Then
            assertThat(image)
                .isEqualTo(
                    expectedImages
                        .get(index++)
                        .stripTrailing()
                );
        }
    }
}
