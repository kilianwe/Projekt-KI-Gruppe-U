import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  BitBoardUtils {
    public static final int BOARD_SIZE = 7;
    private final Map<MovePair, Long> pathMaskMap = new HashMap<>();
    private long[] leftMasks = new long[BOARD_SIZE];
    private long[] rightMasks = new long[BOARD_SIZE];

    public BitBoardUtils(){
        precomputePathMasks();
        long leftMask1 = 1L<<6;
        long rightMask1 = 1L;
        for(int i = 0; i < 6; i++){
            leftMask1 = leftMask1 << 7 | leftMask1;
            rightMask1 = rightMask1 << 7 | rightMask1;
        }
        leftMasks[0] = leftMask1;
        rightMasks[0] = rightMask1;

        for(int i = 1; i <=6; i++){
            leftMasks[i] = leftMasks[i-1] | leftMasks[i-1] >>> 1;
            rightMasks[i] = rightMasks[i-1] | rightMasks[i-1] >>> 1;
        }
    }

    /**
     * Method to check if the Player who has just made a move has won the game.
     * @param player
     * @return boolean
     */
    public boolean checkplayerWon(String player, Board board){
        long playerMask = 0;
        long enemyCastle = 0;
        long enemyMask = 0;
        if(player == "B"){
            playerMask = board.getBlue();
            enemyMask = board.getRed();
            enemyCastle = 1L << 45;
        } else if (player == "R") {
            playerMask = board.getRed();
            enemyMask = board.getBlue();
            enemyCastle = 1L << 3;
        }

        if((board.getGuards() & playerMask) == enemyCastle || (board.getGuards() & playerMask) == (board.getGuards() & enemyMask)){
            return true;
        }
        return false;
    }


    public Board makeMove(MovePair move, Board board){
        long to = (1L << move.getTo());
        System.out.println("To");
        BitBoardUtils.printBitboard(to);

        long from = (1L << move.getFrom());
        System.out.println("From");
        BitBoardUtils.printBitboard(from);

        Board returnBoard = board;
        System.out.println("Stack1");
        BitBoardUtils.printBitboard(returnBoard.getStack(0));

        //Delete "From" Position
        int n = move.getHeight();
        for (int i = 6; i >= 0; i--){
            //If there is a bit present at the "from" position the ^= operation will lead to that bit being deleted which means the height of the Stack at that position will be decreased by 1
            if((returnBoard.getStack(i) | from) == returnBoard.getStack(i)) {
                returnBoard.setStack(i, returnBoard.getStack(i)^ from);
                n--;
            }
            if(n == 0){break;}
        }
        System.out.println("Stack1");
        BitBoardUtils.printBitboard(returnBoard.getStack(0));
        //update blue to include the removal of the "from" position
        returnBoard.setBlue(returnBoard.getBlue() & returnBoard.getStack(0));

        System.out.println("Blue");
        BitBoardUtils.printBitboard(returnBoard.getBlue());


        //delete beaten enemy Stack
        for (int i = 0; i < 7; i++){
            returnBoard.setStack(i, (returnBoard.getStack(i) & returnBoard.getRed() ^ to & returnBoard.getStack(i)) | (returnBoard.getBlue()& returnBoard.getStack(i)) );
        }
        //update red to include the removal of beaten stack
        returnBoard.setRed(returnBoard.getRed() & returnBoard.getStack(0));

        System.out.println("Red");
        BitBoardUtils.printBitboard(returnBoard.getRed());

        //increase Stacks which player who moved owns
        n = move.getHeight();
        for (int i = 0; i < 7; i++){
            //If there is no bit present at the "to" position the | operation will lead to that bit being added which means the height of the Stack at that position will be increased by 1
            if((returnBoard.getStack(i) | to) != returnBoard.getStack(i)) {
                returnBoard.setStack(i, returnBoard.getStack(i) | to);
                n--;
            }
            if(n == 0){break;}
        }
        System.out.println("Stack 1");
        BitBoardUtils.printBitboard(returnBoard.getStack(0));

        //update blue to include the increased Stack
        returnBoard.setBlue(returnBoard.getStack(0) ^ returnBoard.getRed());

        // update guard mask
        if((returnBoard.getGuards() | to) == returnBoard.getGuards()){
            returnBoard.setGuards(returnBoard.getGuards()^from^to);
        }
        return returnBoard;

    }

    /**
     * Generates all Legal Moves in all Directions for a specific player. Boundary Conflicts and jumping violations are handled in generateMovesInDirection.
     * @return List of MovePairs, giving all possible moves in all direction for the current state of the Game.
     */
    public List<MovePair> generateAllLegalMoves(String player, Board board){
        long empty = ~board.getStack(0);
        List<MovePair> moves = new ArrayList<>();
        long playerMask = 0L;
        if(player =="R"){
            playerMask = board.getRed();
        } else if (player == "B") {
            playerMask = board.getBlue();
        }

        for(int i = 0; i < 7; i++) {
            moves.addAll(generateMovesInDirection(board.getStack(i) & playerMask, empty, "N", i+1, board)); // North
            moves.addAll(generateMovesInDirection(board.getStack(i) & playerMask, empty, "S", i+1, board)); // South
            moves.addAll(generateMovesInDirection(board.getStack(i) & playerMask, empty, "E", i+1, board)); // East
            moves.addAll(generateMovesInDirection(board.getStack(i) & playerMask, empty, "W", i+1, board)); // West

        }

        return moves;
    }

    /**
     * Generates all Moves in a specific Direction. Does NOT check if pieces Jump over others for their move. DOES Check if Boundaries are violated.
     * @param fromBits Bitboard containing starting positions of all relevant pieces
     * @param empty Bitboard containing position of empty fields
     * @param dir String giving the Direction for which the moves should be calculated (North-> "N",East-> "E", South-> "S", West-> "W" )
     * @param height int specifying the Minimum height of the Stacks for which the Moves should be calculated. Also determines the Number of steps one Move has.
     * @return List of MovePairs, giving all possible moves which do not violate Boundary's for the specified Direction.
     */
    private List<MovePair> generateMovesInDirection(long fromBits, long empty, String dir, int height, Board board){
        List<MovePair> moves = new ArrayList<>();
        long shifted;
        int shift = 0;

        //check Direction and shift by required amount
        if (dir.equals("E")) {
            shift = height;
            fromBits &= ~leftMasks[height-1];
            shifted = (fromBits << shift) & empty;
        } else if (dir.equals("W")) {
            shift = height;
            fromBits &= ~rightMasks[height-1];
            shifted = (fromBits >>> shift) & empty;
        } else if (dir.equals("N")) {
            shift = 7 * height;
            shifted = (fromBits << shift) & empty;
        } else { // South
            shift = 7 * height;
            shifted = (fromBits >>> shift) & empty;
        }
        //extract from -> to sequences from shifted Bitboard
        while (shifted != 0){
            int to = Long.numberOfTrailingZeros(shifted);
            int from = to -shift;
            MovePair move = new MovePair(from, to, height);
            //Checking for jumping violations and out of bounds violations
            if(from >= 0 && from < 49 ) {
                moves.add(move);
            }
            shifted &= shifted -1; //niedrigstes Bit löschen
        }

        return moves;

    }

    private void precomputePathMasks() {
        for (int from = 0; from < 49; from++) {
            int x1 = from % BOARD_SIZE;
            int y1 = from / BOARD_SIZE;
            int height = 0;

            for (int to = 0; to < 49; to++) {
                if (from == to) continue;

                int x2 = to % BOARD_SIZE;
                int y2 = to / BOARD_SIZE;

                // Nur orthogonal (N, S, E, W)
                if (x1 == x2 || y1 == y2) {
                    long mask = 0L;

                    // Vertikal
                    if (x1 == x2) {
                        int yStart = Math.min(y1, y2) + 1;
                        int yEnd = Math.max(y1, y2);
                        for (int y = yStart; y < yEnd; y++) {
                            int index = y * BOARD_SIZE + x1;
                            mask |= 1L << index;
                        }
                        height = yEnd - (yStart-1);
                    }

                    // Horizontal
                    if (y1 == y2) {
                        int xStart = Math.min(x1, x2) + 1;
                        int
                                xEnd = Math.max(x1, x2);
                        for (int x = xStart; x < xEnd; x++) {
                            int index = y1 * BOARD_SIZE + x;
                            mask |= 1L << index;
                        }
                        height = xEnd - (xStart-1);
                    }

                    pathMaskMap.put(new MovePair(from, to,height), mask);
                }
            }
        }
    }


    private boolean moveDoesntJump(MovePair move, Board board){
        return (board.getStack(0) ^ pathMaskMap.get(move) & board.getStack(0)) == board.getStack(0);
    }
    public static void printBitboard(long bitboard) {
        final int BOARD_SIZE = 7;
        System.out.println("Bitboard-Darstellung:");

        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                int index = y * BOARD_SIZE + x;
                boolean isSet = ((bitboard >> index) & 1L) != 0;
                System.out.print(isSet ? "1 " : ". ");
            }
            System.out.println();
        }
    }





    /**
     * Hilfsklasse um Züge besser speichern zu können
     */
    static class MovePair {
        private final int from;
        private final int to;
        private final int height;

        /**
         * Konstruktor der Klasse MovePair
         * @param from int-Repräsentation des Start-Feldes eines Zuges
         * @param to int-Repräsentation des End-Feldes eines Zuges
         */
        public MovePair(int from, int to, int height){
            this.from = from;
            this.to = to;
            this.height = height;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getHeight() {
            return height;
        }

        /**
         * Methode um MovePairs miteinander zu vergleichen
         * @param o anderes MovePair, mit dem dieses vergleichen werden soll
         * @return Boolscher Wert der angibt ob die beiden MovePairs die Instanzvariablen der beiden MovePairs gleich sind
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MovePair)) return false;
            MovePair p = (MovePair) o;
            return from == p.from && to == p.to;
        }

        public Move toMove(){
            int fromRow = this.from % BOARD_SIZE;
            int fromCol = this.from / BOARD_SIZE;
            int toRow = this.to % BOARD_SIZE;
            int toCol = this.to / BOARD_SIZE;
            int moveHeight = this.height;

            return new Move(fromRow, fromCol, toRow, toCol, moveHeight);
        }
    }
}