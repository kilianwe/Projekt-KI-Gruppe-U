import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /**
        Board bitboard0 = new Board();
        BitBoardUtils utils = new BitBoardUtils();
        List<BitBoardUtils.MovePair> movePairs = utils.generateAllLegalMoves("R", bitboard0);
        System.out.println(movePairs);
        for(BitBoardUtils.MovePair pair : movePairs){
            System.out.println(pair.toMove() + " -> " + pair.toMove().toAlgebraic());
            System.out.println(pair);
        }
        System.out.println(movePairs.size() + " moves generated.");




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


    */
    Board board = new Board();
    BitBoardUtils utils = new BitBoardUtils();
    board.printBoard();
    int numberOfTurns = 3;
    String currentPlayer = "B";
    while(!(utils.checkplayerWon("B", board) || utils.checkplayerWon("R", board)) && numberOfTurns != 0){

        List<BitBoardUtils.MovePair> moves = utils.generateAllLegalMoves(currentPlayer,board);
        BitBoardUtils.MovePair chosenMove = utils.pickMove(moves, board);
        board = utils.makeMove(chosenMove,board);
        board.printBoard();

        if(currentPlayer == "B"){
            currentPlayer = "R";
        }else{
            currentPlayer = "B";
        }
        numberOfTurns--;
    }
    }
}