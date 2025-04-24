import java.util.ArrayList;
import java.util.List;

/** Utility class – never instantiated. */
public final class GuardTowersMoveGenerator {

    private static final int BOARD_SIZE = 7;                // 7 × 7 board
    private static final int[][] DIRS = {                   // N, S, W, E
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private GuardTowersMoveGenerator() { }                  // prevent instantiation

    /**
     * Generate every pseudo-legal move for the side indicated by {@code forRed}.
     *
     * @param board  current position, {@code null} means empty square
     * @param forRed {@code true} = generate red moves, {@code false} = blue
     * @return list of legal moves according to the rules of Guard & Towers
     */
    public static List<Move> generateMoves(Piece[][] board, boolean forRed) {
        List<Move> moves = new ArrayList<>();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece p = board[r][c];
                if (p == null || p.isRed() != forRed) continue;

                switch (p.getType()) {
                    case GUARD -> addGuardMoves(board, r, c, moves, forRed);
                    case TOWER -> addTowerMoves(board, r, c, p.getHeight(), moves, forRed);
                }
            }
        }
        return moves;
    }

    /* ========== helpers ========== */

    private static void addGuardMoves(Piece[][] board, int r, int c,
                                      List<Move> moves, boolean forRed) {
        for (int[] d : DIRS) {
            int nr = r + d[0], nc = c + d[1];
            if (!inBounds(nr, nc)) continue;

            Piece target = board[nr][nc];
            if (target == null || target.isRed() != forRed) {
                // empty square or capture of any enemy figure
                moves.add(new Move(r, c, nr, nc, 1));
            }
        }
    }

    private static void addTowerMoves(Piece[][] board, int r, int c, int height,
                                      List<Move> moves, boolean forRed) {
        // moveHeight = number of stones moved (1 … full height)
        for (int moveHeight = 1; moveHeight <= height; moveHeight++) {
            for (int[] d : DIRS) {
                int destR = r + d[0] * moveHeight;
                int destC = c + d[1] * moveHeight;
                if (!inBounds(destR, destC)) continue;

                // Path must be empty except possibly at destination
                if (!pathClear(board, r, c, d, moveHeight)) continue;

                Piece target = board[destR][destC];

                if (target == null) {
                    // simple move / unstacking
                    moves.add(new Move(r, c, destR, destC, moveHeight));

                } else if (target.isRed() == forRed) {
                    // friendly piece – stacking only allowed onto a friendly tower
                    if (target.getType() == Piece.Type.TOWER) {
                        moves.add(new Move(r, c, destR, destC, moveHeight));
                    }

                } else { // enemy piece – capture checks
                    switch (target.getType()) {
                        case GUARD -> moves.add(new Move(r, c, destR, destC, moveHeight));
                        case TOWER -> {
                            if (moveHeight >= target.getHeight()) {
                                moves.add(new Move(r, c, destR, destC, moveHeight));
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean pathClear(Piece[][] board, int r, int c,
                                     int[] d, int distance) {
        for (int step = 1; step < distance; step++) {
            int nr = r + d[0] * step;
            int nc = c + d[1] * step;
            if (board[nr][nc] != null) return false;
        }
        return true;
    }

    private static boolean inBounds(int r, int c) {
        return r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE;
    }
}
