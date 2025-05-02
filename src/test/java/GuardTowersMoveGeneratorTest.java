import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuardTowersMoveGeneratorTest {

    private void assertMoveExists(List<Move> moves, int fromRow, int fromCol, int toRow, int toCol, int moveHeight) {
        boolean found = moves.stream().anyMatch(m ->
                m.fromRow == fromRow && m.fromCol == fromCol &&
                        m.toRow == toRow && m.toCol == toCol &&
                        m.moveHeight == moveHeight
        );
        assertTrue(found, String.format("Erwarteter Zug von (%d,%d) nach (%d,%d) mit moveHeight %d wurde nicht gefunden.",
                fromRow, fromCol, toRow, toCol, moveHeight));
    }

    @Test
    public void testExpectedMovesFromStartingPosition() {
        Piece[][] board = BoardInitializer.createStartingPosition();

        List<Move> moves = GuardTowersMoveGenerator.generateMoves(board, true);

        assertEquals(25, moves.size(), "Es sollten genau 21 Züge generiert werden.");

        // Die 21 erwarteten Züge:
        assertMoveExists(moves, 0, 0, 1, 0, 1);  // A7 -> A6
        assertMoveExists(moves, 0, 0, 0, 1, 1);  // A7 -> B7
        assertMoveExists(moves, 0, 1, 1, 1, 1);  // B7 -> B6
        assertMoveExists(moves, 0, 1, 0, 2, 1);  // B7 -> C7
        assertMoveExists(moves, 1, 2, 1, 1, 1);  // C6 -> B6
        assertMoveExists(moves, 1, 2, 2, 2, 1);  // C6 -> C5
        assertMoveExists(moves, 1, 2, 0, 2, 1);  // C6 -> C7
        assertMoveExists(moves, 1, 2, 1, 3, 1);  // C6 -> D6
        assertMoveExists(moves, 0, 3, 1, 3, 1);  // D7 -> D6
        assertMoveExists(moves, 0, 3, 0, 2, 1);  // D7 -> C7
        assertMoveExists(moves, 0, 3, 0, 4, 1);  // D7 -> E7
        assertMoveExists(moves, 0, 5, 1, 5, 1);  // F7 -> F6
        assertMoveExists(moves, 0, 5, 0, 4, 1);  // F7 -> E7
        assertMoveExists(moves, 0, 5, 0, 6, 1);  // F7 -> G7
        assertMoveExists(moves, 0, 6, 1, 6, 1);  // G7 -> G6
        assertMoveExists(moves, 0, 6, 0, 5, 1);  // G7 -> F7
        assertMoveExists(moves, 1, 4, 0, 4, 1);  // E6 -> E7
        assertMoveExists(moves, 1, 4, 2, 4, 1);  // E6 -> E5
        assertMoveExists(moves, 1, 4, 1, 3, 1);  // E6 -> D6
        assertMoveExists(moves, 1, 4, 1, 5, 1);  // E6 -> F6
        assertMoveExists(moves, 2, 3, 1, 3, 1);  // D5 -> D6
    }
}
