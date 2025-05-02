import java.util.Objects;

public final class VictoryChecker {

    public enum Winner { RED, BLUE, NONE }

    private static final int BOARD_SIZE = 7;
    private static final int RED_CASTLE_ROW = 0, RED_CASTLE_COL = 3;
    private static final int BLUE_CASTLE_ROW = 6, BLUE_CASTLE_COL = 3;

    /**
     * Determine which player (if any) has already won the game.
     *
     * @param board the current position, using the same 7 x 7 array layout as everywhere else in the project
     * @return {@link Winner#RED} – red has won;
     *         {@link Winner#BLUE} – blue has won;
     *         {@link Winner#NONE} – no winner yet
     */
    public static Winner checkWinner(Piece[][] board) {
        Objects.requireNonNull(board, "board must not be null");

        boolean redGuardPresent = false;
        boolean blueGuardPresent = false;
        int redGuardRow = -1, redGuardCol = -1;
        int blueGuardRow = -1, blueGuardCol = -1;

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece p = board[r][c];
                if (p == null || p.getType() != Piece.Type.GUARD) continue;

                if (p.isRed()) {
                    redGuardPresent = true;
                    redGuardRow = r;
                    redGuardCol = c;
                } else {
                    blueGuardPresent = true;
                    blueGuardRow = r;
                    blueGuardCol = c;
                }
            }
        }

        /* ---------- guard captured ---------- */
        if (!redGuardPresent && blueGuardPresent) return Winner.BLUE;
        if (!blueGuardPresent && redGuardPresent) return Winner.RED;

        /* ---------- guard on opponent’s castle ---------- */
        if (redGuardPresent && redGuardRow == BLUE_CASTLE_ROW && redGuardCol == BLUE_CASTLE_COL)
            return Winner.RED;
        if (blueGuardPresent && blueGuardRow == RED_CASTLE_ROW && blueGuardCol == RED_CASTLE_COL)
            return Winner.BLUE;

        return Winner.NONE;
    }

    /**
     * Convenience wrapper around {@link #checkWinner(Piece[][])}.
     *
     * @param board the current position
     * @return {@code true} if the game is over (either side has already won)
     */
    public static boolean isGameOver(Piece[][] board) {
        return checkWinner(board) != Winner.NONE;
    }

    //‑‑‑‑‑‑‑‑‑‑ prevent instantiation ‑‑‑‑‑‑‑‑‑‑
    private VictoryChecker() { }
}