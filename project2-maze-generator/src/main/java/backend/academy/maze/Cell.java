package backend.academy.maze;

import backend.academy.util.PureFunctional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Simple maze cell.
 *
 * @author alnmlbch
 */
@PureFunctional
@RequiredArgsConstructor
@Getter
public enum Cell {
    WALL("wall"),
    PATH("path"),
    SOLUTION("solution"),
    START("start"),
    FINISH("finish"),
    COIN("coin"),
    BANK("bank");

    private final String log;
}
