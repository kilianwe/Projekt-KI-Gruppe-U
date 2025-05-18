import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class BoardTest {
    @Test
    public void boardFromFenTest(){
        Board startBoard = new Board();
        Board fenBoard = new Board("r1r11RG1r1r1/2r11r12/3r13/7/3b13/2b11b12/b1b11BG1b1b1 r");
        System.out.println("StartBoard");
        startBoard.printBoard();
        System.out.println("FenBoard");
        fenBoard.printBoard();
        assertEquals(startBoard, fenBoard);
    }
}
