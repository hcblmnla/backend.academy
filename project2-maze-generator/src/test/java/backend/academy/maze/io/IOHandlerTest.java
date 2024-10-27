package backend.academy.maze.io;

import backend.academy.maze.Cell;
import backend.academy.maze.Ctor;
import backend.academy.maze.algo.BfsSolver;
import backend.academy.maze.algo.Generator;
import backend.academy.maze.algo.KruskalGenerator;
import backend.academy.maze.render.StaticRenderer;
import backend.academy.maze.render.Theme;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class IOHandlerTest {

    @Test
    @SneakyThrows
    void simpleMockReaderTest() {
        // Given
        final var reader = Mockito.mock(LineReader.class);
        // When
        Mockito.when(reader.readLine(Mockito.any(String.class)))
            .thenReturn("1")
            .thenReturn("2")
            .thenReturn("1")
            .thenReturn("10")
            .thenReturn("-1")
            .thenReturn("xyz")
            .thenReturn("11")
            .thenReturn("13")
            .thenReturn("1")
            .thenReturn("2")
            .thenReturn("2")
        ;
        // Then
        final var io = IOHandler.of(reader);
        assertThat(io.getProcess()).isEqualTo(Handler.Process.GENERATING);
        assertThat(io.getGenerator()).isInstanceOf(KruskalGenerator.class);
        assertThat(io.getGenerationType()).isEqualTo(Generator.Type.CLASSIC);
        io.print("testing message 1");
        assertThat(io.getParam("height")).isEqualTo(11);
        assertThat(io.getParam("width")).isEqualTo(13);
        assertThat(io.getRenderer()).isInstanceOf(StaticRenderer.class);
        assertThat(io.getTheme()).isEqualTo(Theme.COLORFUL);
        assertThat(io.getSolver(Ctor.ctorFromGrid(
                1, 1,
                Cell.WALL, Cell.WALL, Cell.WALL,
                Cell.START, Cell.PATH, Cell.FINISH,
                Cell.WALL, Cell.WALL, Cell.WALL
            )
            .call()))
            .isInstanceOf(BfsSolver.class);
        io.mazeCannotBeInitialized("testing message 2");
    }
}
