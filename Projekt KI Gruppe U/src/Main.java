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

        Board bitboard0 = new Board();
        BitBoardUtils utils = new BitBoardUtils();
        bitboard0.printBoard();
        utils.printBitboard(bitboard0.getBlue());
        utils.printBitboard(bitboard0.getRed());
        utils.printBitboard(bitboard0.getGuards());
        for (int i = 0; i < 7; i++) {
            utils.printBitboard(bitboard0.getStack(i));
        }
        BitBoardUtils.MovePair move = new BitBoardUtils.MovePair(0,7,1);
        Board bitBoard1 = utils.makeMove(move, bitboard0);
        bitBoard1.printBoard();
    }


}