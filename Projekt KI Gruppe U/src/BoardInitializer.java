public class BoardInitializer {

    /**
     * Build the Guard & Towers initial layout.
     *
     * Coordinates:
     *   columns 0-6 == A-G (left→right)
     *   rows    0-6 == 7-1 (top→bottom)               ┌─ 7 ─┐
     *                                                …middle…
     *   red (true) starts at the top (row 0);         └─ 1 ─┘
     */
    public static Piece[][] createStartingPosition() {
        Piece[][] board = new Piece[7][7];

        /* ---------- red player (top, rows 0-2) ---------- */
        // baseline row 0 – A7 B7 D7 F7 G7
        board[0][0] = new Piece(Piece.Type.TOWER, 1, true);          // A7
        board[0][1] = new Piece(Piece.Type.TOWER, 1, true);          // B7
        board[0][3] = new Piece(Piece.Type.GUARD,  1, true);         // D7 (castle)
        board[0][5] = new Piece(Piece.Type.TOWER, 1, true);          // F7
        board[0][6] = new Piece(Piece.Type.TOWER, 1, true);          // G7

        // row 1 – C6 E6
        board[1][2] = new Piece(Piece.Type.TOWER, 1, true);          // C6
        board[1][4] = new Piece(Piece.Type.TOWER, 1, true);          // E6

        // row 2 – D5
        board[2][3] = new Piece(Piece.Type.TOWER, 1, true);          // D5


        /* ---------- black player (bottom, rows 4-6) ---------- */
        // row 4 – D3
        board[4][3] = new Piece(Piece.Type.TOWER, 1, false);         // D3

        // row 5 – C2 E2
        board[5][2] = new Piece(Piece.Type.TOWER, 1, false);         // C2
        board[5][4] = new Piece(Piece.Type.TOWER, 1, false);         // E2

        // baseline row 6 – A1 B1 D1 F1 G1
        board[6][0] = new Piece(Piece.Type.TOWER, 1, false);         // A1
        board[6][1] = new Piece(Piece.Type.TOWER, 1, false);         // B1
        board[6][3] = new Piece(Piece.Type.GUARD,  1, false);        // D1 (castle)
        board[6][5] = new Piece(Piece.Type.TOWER, 1, false);         // F1
        board[6][6] = new Piece(Piece.Type.TOWER, 1, false);         // G1

        return board;
    }


    public static String toString(Piece[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (board[row][col] == null) {
                    sb.append(" .  ");
                } else {
                    sb.append(String.format("%-3s", board[row][col].toString()));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
