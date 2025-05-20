import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
public class MinimaxTest {

    @Test
    void minimaxTest(){
        Board board = new Board("3r13/2b12r21/b16/2RG1b22/b16/2b22BG1/3r23 r");
        BitBoardUtils utils = new BitBoardUtils();
        AtomicInteger stateCounter = new AtomicInteger();
        long start = System.currentTimeMillis();
        System.out.println(BitBoardUtils.minimax(board,false, 1, stateCounter));
        long duration = System.currentTimeMillis() - start;
        System.out.println("Minimax beendet:");
        System.out.println("Dauer: " + duration + " ms");
        System.out.println("Bewertete Zustände: " + stateCounter.get());
    }

    @Test
    void minimaxAlphaBetaTest(){
        Board board = new Board("3RG3/7/7/7/4b11b1/4r4r11/3BG1b11 b");
        BitBoardUtils utils = new BitBoardUtils();

        System.out.println(utils.minimaxAlphaBeta(board,false, Integer.MIN_VALUE, Integer.MAX_VALUE, System.currentTimeMillis(), 1000));
    }

}
