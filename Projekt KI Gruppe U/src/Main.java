import java.util.List;

public class Main {
    public static void main(String[] args) {
        Piece[][] board = BoardInitializer.createStartingPosition();
        System.out.println(BoardInitializer.toString(board));

        // Generate and print all moves for white
        List<Move> whiteMoves = GuardTowersMoveGenerator.generateMoves(board, true);
        for (Move move : whiteMoves) {
            System.out.println(move + " -> " + move.toAlgebraic());
        }
        System.out.println(whiteMoves.size() + " moves generated.");

        Board bitboard = new Board();
    }


}