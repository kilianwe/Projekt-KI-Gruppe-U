import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class MakeMoveTest {

    @Test
    void makeMoveTestAddTowers(){
        Board before = new Board("3RG3/7/7/7/4b11b1/4r4r11/3BG1b11 r");
        Board after = new Board("3RG3/7/7/7/4b11b1/4r52/3BG1b11 b");
        BitBoardUtils utils = new BitBoardUtils();
        BitBoardUtils.MovePair move = new BitBoardUtils.MovePair(8,9,1);
        assertEquals(utils.makeMove(move, before), after);
    }

    @Test
    void makeMoveTestBeatStack(){
        Board before = new Board("3RG3/7/7/7/4b11b1/4r4r11/3BG1b11 r");
        Board after = new Board("3RG3/7/7/7/4r11b1/4r3r11/3BG1b11 b");
        BitBoardUtils utils = new BitBoardUtils();
        BitBoardUtils.MovePair move = new BitBoardUtils.MovePair(9,16,1);
        assertTrue(BitBoardUtils.makeMove(move, before).equals(after));
    }

    @Test
    void makeMoveTestBeatGuard(){
        Board before = new Board("3RG3/7/7/7/4b11b1/3r41r11/3BG1b11 r");
        Board after = new Board("3RG3/7/7/7/4b11b1/3r31r11/3r11b11 b");
        BitBoardUtils utils = new BitBoardUtils();
        BitBoardUtils.MovePair move = new BitBoardUtils.MovePair(10,3,1);
        Board outcome = utils.makeMove(move, before);
        assertEquals(outcome, after);

    }

    @Test
    void makeMoveTestGuardBeatsGuard(){
        Board before = new Board("7/7/7/7/7/7/5RGBG b");
        Board after = new Board("7/7/7/7/7/7/5BG1 r");
        BitBoardUtils utils = new BitBoardUtils();
        BitBoardUtils.MovePair move = new BitBoardUtils.MovePair(0,1,1);
        Board outcome = utils.makeMove(move, before);
        assertEquals(outcome, after);
    }

    @Test
    void testCheckPlayerWon(){
        Board before = new Board("7/7/7/7/7/7/5BG1 r");
        assertTrue(BitBoardUtils.checkplayerWon(before, Player.BLUE));
    }


}
