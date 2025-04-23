import java.util.List;

public class Main {
    public static void main(String[] args) {
        Piece[][] board = new Piece[7][7];

        // Place a white guard at position (6, 3) (equivalent to d1 in algebraic)
        board[6][3] = new Piece(Piece.Type.GUARD, 1, true);

        // Place a white tower of height 2 at (5, 3) (d2)
        board[5][3] = new Piece(Piece.Type.TOWER, 2, true);

        // Place a black guard at (3, 3) (d4)
        board[3][3] = new Piece(Piece.Type.GUARD, 1, false);

        // Place a black tower of height 1 at (5, 5) (f2)
        board[5][5] = new Piece(Piece.Type.TOWER, 1, false);

        // Generate and print all moves for white
        List<Move> whiteMoves = GuardTowersMoveGenerator.generateMoves(board, true);
        for (Move move : whiteMoves) {
            System.out.println(move + " -> " + move.toAlgebraic());
        }
    }
}