import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
public class MinimaxTest {

    @Test
    void minimaxTest(){
        Board board = new Board("7/6r3/1RG5/3b43/1r25/7/2BG3r1 r");
        BitBoardUtils utils = new BitBoardUtils();
        AtomicInteger stateCounter = new AtomicInteger();
        long start = System.currentTimeMillis();
        System.out.println(BitBoardUtils.minimax(board,6, false, stateCounter));
        long duration = System.currentTimeMillis() - start;
        System.out.println("Minimax beendet:");
        System.out.println("Dauer: " + duration + " ms");
        System.out.println("Bewertete Zustände: " + stateCounter.get());
    }

    @Test
    void minimaxAlphaBetaTest(){
        Board board = new Board("7/6r3/1RG5/3b43/1r25/7/2BG3r1 r19");
        BitBoardUtils utils = new BitBoardUtils();

        System.out.println(utils.pickMove(board).toMove().toAlgebraic());
    }

    @Test
    void benchmarkEvaluate(){
        Board board = new Board("b36/3b12r3/7/7/1r2RG4/2BG4/6r1 b");
        long start = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++){
            BitBoardUtils.evaluate(board);
        }
        System.out.println("Dauer: " + (System.currentTimeMillis() - start));
    }

}
