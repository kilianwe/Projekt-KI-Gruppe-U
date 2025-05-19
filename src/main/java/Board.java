import java.util.*;

public class Board {

    private long guards;
    private long blue;
    private long red;
    // Stacks indicate the minimum of how many pieces a tower contains
    // a Tower with three pieces has "1" entries in Stacks 0,1,2 and "0" entries in all Stacks above
    private long[] stacks = {0L, 0L, 0L, 0L, 0L, 0L, 0L};
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
        this.blue = 1L | 1L << 1 | 1L << 3 | 1L << 5 | 1L << 6 | 1L << 9 | 1L << 11 | 1L << 17;
        this.red = 1L << 31 | 1L << 37 | 1L << 39 | 1L << 42 | 1L << 43 | 1L << 45 | 1L << 47 | 1L << 48;
        this.stacks[0] = this.blue | this.red;
        for (int i = 1; i < 7; i++) {
            this.stacks[i] = 0L;
        }
        this.currentPlayer = Player.RED;
    }

    public Board(String fen) {
        String[] splitFen = fen.split(" ");
        String positionString = splitFen[0];
        String playerString = splitFen[1];

        this.guards = 0L;
        this.blue = 0L;
        this.red = 0L;

        int boardIndex = 48;
        for (int i = 0; i < positionString.length(); ) {
            char c = positionString.charAt(i);

            if (Character.isDigit(c)) {
                // Empty Fields
                int empty = c - '0';
                boardIndex -= empty;
                i++;
            } else if ((c == 'r') && i + 1 < positionString.length() && Character.isDigit(positionString.charAt(i + 1))) {
                // Red Tower
                int height = positionString.charAt(i + 1) - '0' - 1;
                // populate Stack corresponding to height
                stacks[height] = stacks[height] | (1L << boardIndex);
                red |= (1L << boardIndex);
                // move String index
                i += 2;
                // move Board index
                boardIndex -= 1;
            } else if ((c == 'b') && i + 1 < positionString.length() && Character.isDigit(positionString.charAt(i + 1))) {
                // Blue Tower
                int height = positionString.charAt(i + 1) - '0' - 1;
                // populate Stack corresponding to height and color Bitboard
                stacks[height] = stacks[height] | (1L << boardIndex);
                blue |= (1L << boardIndex);
                // move String index
                i += 2;
                // move Board index
                boardIndex -= 1;
            } else if (c == 'R') {
                // Red Guard
                //populate guard and color bitboard
                guards |= 1L << boardIndex;
                red |= (1L << boardIndex);
                // move String index
                i += 2;
                //move board index
                boardIndex -= 1;
            } else if (c == 'B') {
                // Blue Guard
                //populate guard and color bitboard
                guards |= 1L << boardIndex;
                blue |= (1L << boardIndex);
                // move String index
                i += 2;
                //move board index
                boardIndex -= 1;
            } else if (c == '/') {
                i += 1;
            } else {
                throw new IllegalArgumentException("Unbekanntes FEN-Element bei Index " + i + ": " + positionString.substring(i));
            }
        }
        //current Player setzen
        if (Objects.equals(playerString, "r")) {
            this.currentPlayer = Player.RED;
        } else if (Objects.equals(playerString, "b")){
            this.currentPlayer = Player.BLUE;
        }

        // connect stacks so that they indicate minimum height and not absolute height
        this.stacks[0] = stacks[0] | stacks[1] | stacks[2] | stacks[3] | stacks[4] | stacks[5] | stacks[6] | guards;
        this.stacks[1] = stacks[1] | stacks[2] | stacks[3] | stacks[4] | stacks[5] | stacks[6];
        this.stacks[2] = stacks[2] | stacks[3] | stacks[4] | stacks[5] | stacks[6];
        this.stacks[3] = stacks[3] | stacks[4] | stacks[5] | stacks[6];
        this.stacks[4] = stacks[4] | stacks[5] | stacks[6];
        this.stacks[5] = stacks[5] | stacks[6];
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Board) || o == null) {
                return false;
            }
            Boolean guardsEqual = this.guards == ((Board) o).getGuards();
            Boolean blueEqual = this.blue == ((Board) o).getBlue();
            Boolean redEqual = this.red == ((Board) o).getRed();
            Boolean currentPlayerEqual = this.currentPlayer == ((Board) o).getCurrentPlayer();
            Boolean stacksEqual = true;
            for (int i = 0; i < 7; i++) {
                stacksEqual = stacksEqual && this.stacks[i] == ((Board) o).getStack(i);
            }
            return guardsEqual && blueEqual && redEqual && currentPlayerEqual && stacksEqual;
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(this.blue, this.red, this.guards);
            result = 31 * result + Arrays.hashCode(this.stacks);
            return result;
        }


        public void printBoard() {
            final int BOARD_SIZE = 7;
            System.out.println("Aktueller Spielstand:");

        for (int i = 48; i >= 0; i--) {
            String symbol = "__";
            // Guard hat Vorrang
            if (((guards >> i) & 1L) != 0) {
                if(((blue >> i) & 1L) != 0) {
                    symbol = "BG";
                } else if (((red >> i) & 1L) != 0) {
                    symbol = "RG";
                }
            }
            // Sonst Red
            else if (((red >> i) & 1L) != 0) {
                int stackHeight = 0;
                for (int h = 0; h < stacks.length; h++) {
                    if (((stacks[h] >> i) & 1L) != 0) {
                        stackHeight = h + 1;
                    }
                }
                symbol = "R" + stackHeight;
            }
            // Sonst Blue
            else if (((blue >> i) & 1L) != 0) {
                int stackHeight = 0;
                for (int h = 0; h < stacks.length; h++) {
                    if (((stacks[h] >> i) & 1L) != 0) {
                        stackHeight = h + 1;
                    }
                }
                symbol = "B" + stackHeight;
            }

            // Ausgabeformat: Symbol + Höhe
            System.out.printf("%s", symbol);
            if(i % 7 == 0){
                System.out.println();
            }
        }
    }

    /**
     *
     * @param board from which the number of pieces should be calculated
     * @param player for which the number of pieces should be calculated
     * @return
     */
    public int numPieces(Player player){
        int numPiece = 0;
        long playerMask = 0;
        if(player == Player.RED){
            playerMask = this.red;
        }else if(player == Player.BLUE){
            playerMask = this.blue;
        }

        for (int i = 0; i < 7; i++){
            long colorStackI = stacks[i] & playerMask;
            for (int j = 0; j < 49; j++){
                if (((colorStackI >>> j) & 1L) != 0){
                    numPiece += 1;
                }
            }
        }
        return numPiece;
    }

    public Board copy() {
        Board b = new Board();
        b.setBlue(this.blue);
        b.setRed(this.red);
        b.setGuards(this.guards);
        long[] newStacks = new long[7];
        for (int i = 0; i < 7; i++) {
            newStacks[i] = this.stacks[i];
        }
        return b;
    }
}
