import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorTest {
    public static List<Move> parseMoves(List<String> moveStrings) {
        List<Move> moves = new ArrayList<>();
        for (String moveStr : moveStrings) {
            moves.add(parseMove(moveStr));
        }
        return moves;
    }

    public static Move parseMove(String moveStr) {
        // Format: "G6-G7-1"
        char fromColChar = moveStr.charAt(0);
        int fromRowNum = Character.getNumericValue(moveStr.charAt(1));
        char toColChar = moveStr.charAt(3);
        int toRowNum = Character.getNumericValue(moveStr.charAt(4));
        int height = Character.getNumericValue(moveStr.charAt(6));

        int fromCol = fromColChar - 'A';
        int fromRow = 7 - fromRowNum; // Zeile 1 → Index 6, Zeile 7 → Index 0
        int toCol = toColChar - 'A';
        int toRow = 7 - toRowNum;

        return new Move(fromRow, fromCol, toRow, toCol, height);
    }
    @Test
    void moveGeneratorTest(){
        Board board = new Board("r1r11RG1r1r1/2r11r12/3r13/7/3b13/2b11b12/b1b11BG1b1b1 r");
        BitBoardUtils utils = new BitBoardUtils();
        List<BitBoardUtils.MovePair> movePairs = utils.generateAllLegalMoves(board);
        List<Move> generatedMoves = new ArrayList<>();
        for(BitBoardUtils.MovePair pair : movePairs){
            generatedMoves.add(pair.toMove());
        }
        List<String> actualMoves = List.of(
                "A7-A6-1",
                "A7-B7-1",
                "B7-A7-1",
                "B7-B6-1",
                "B7-C7-1",
                "C6-B6-1",
                "C6-C5-1",
                "C6-C7-1",
                "C6-D6-1",
                "D7-C7-1",
                "D7-D6-1",
                "D7-E7-1",
                "D5-C5-1",
                "D5-D4-1",
                "D5-D6-1",
                "D5-E5-1",
                "E6-D6-1",
                "E6-E5-1",
                "E6-E7-1",
                "E6-F6-1",
                "F7-E7-1",
                "F7-F6-1",
                "F7-G7-1",
                "G7-F7-1",
                "G7-G6-1"
        );
        List<Move> actual = parseMoves(actualMoves);
        assertTrue(generatedMoves.containsAll(actual));


    }

}
