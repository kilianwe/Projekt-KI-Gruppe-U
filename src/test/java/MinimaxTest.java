import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class MinimaxTest {

    @Test
    void minimaxTest(){
        Board board = new Board("3RG3/7/7/7/4b11b1/4r4r11/3BG1b11 b");
        BitBoardUtils utils = new BitBoardUtils();
        System.out.println(utils.minimax(board,true));
    }

    @Test
    void minimaxAlphaBetaTest(){
        Board board = new Board("3RG3/7/7/7/4b11b1/4r4r11/3BG1b11 b");
        BitBoardUtils utils = new BitBoardUtils();

        System.out.println(utils.minimaxAlphaBeta(board,true, Integer.MIN_VALUE, Integer.MAX_VALUE, System.currentTimeMillis(), 1000));
    }

}
