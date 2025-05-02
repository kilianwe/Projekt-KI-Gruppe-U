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
        BitBoardUtils.MovePair move = new BitBoardUtils.MovePair(0,1,1);
        Board bitBoard1 = utils.makeMove(move, bitboard0);
        bitBoard1.printBoard();
        move = new BitBoardUtils.MovePair(1,15,2);
        Board bitBoard2 = utils.makeMove(move,bitBoard1);
        bitBoard2.printBoard();

        move = new BitBoardUtils.MovePair(15,17,2);
        Board bitBoard3 = utils.makeMove(move, bitBoard2);
        bitBoard3.printBoard();

        move = new BitBoardUtils.MovePair(17,31,2);
        Board bitBoard4 = utils.makeMove(move, bitBoard3);
        bitBoard4.printBoard();



    }


}