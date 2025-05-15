import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private long guards;
    private long blue;
    private long red;
    private long[] stacks = new long[7];
    private Player currentPlayer;

    public void setGuards(long guards) {
        this.guards = guards;
    }

    public void setBlue(long blue) {
        this.blue = blue;
    }

    public void setRed(long red) {
        this.red = red;
    }

    public void setStack(int i, long stack) {
        this.stacks[i] = stack;
    }

    /**
     * Constructor to create a specific Board according to Parameters
     */
    public Board(long guards, long blue, long red, long[] stacks, Player player) {
        this.guards = guards;
        this.blue = blue;
        this.red = red;
        this.stacks = stacks;
        this.currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Constructor to create the starting Board
     */
    public Board() {
        this.guards = 1L << 3 | 1L << 45;
        this.red = 1L | 1L << 1 | 1L << 3 | 1L << 5 | 1L << 6 | 1L << 9 | 1L << 11 | 1L << 17;
        this.blue = 1L << 31 | 1L << 37 | 1L << 39 | 1L << 42 | 1L << 43 | 1L << 45 | 1L << 47 | 1L << 48;
        this.stacks[0] = this.blue | this.red;
        for (int i = 1; i < 7; i++) {
            this.stacks[i] = 0L;
        }
        this.currentPlayer = Player.BLUE;
    }

    public Board(String fen){

    }

    public long getGuards() {
        return guards;
    }

    public long getBlue() {
        return blue;
    }

    public long getRed() {
        return red;
    }

    public long getStack(int i) {
        return stacks[i];
    }


    public void printBoard() {
        final int BOARD_SIZE = 7;
        System.out.println("Aktueller Spielstand:");

        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                int index = y * BOARD_SIZE + x;
                char symbol = '.';

                // Guard hat Vorrang
                if (((guards >> index) & 1L) != 0) {
                    symbol = 'G';
                }
                // Sonst Red
                else if (((red >> index) & 1L) != 0) {
                    symbol = 'R';
                }
                // Sonst Blue
                else if (((blue >> index) & 1L) != 0) {
                    symbol = 'B';
                }

                // Stackhöhe ermitteln
                int stackHeight = 0;
                for (int h = 0; h < stacks.length; h++) {
                    if (((stacks[h] >> index) & 1L) != 0) {
                        stackHeight = h + 1;
                    }
                }

                // Ausgabeformat: Symbol + Höhe
                System.out.printf("%s%d ", symbol, stackHeight);
            }
            System.out.println();
        }
    }
}