import java.util.*;

public class GuardTowersMoveGenerator {
    static final int BOARD_SIZE = 7;
    static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

    public static List<Move> generateMoves(Piece[][] board, boolean forWhite) {
        List<Move> moves = new ArrayList<>();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = board[r][c];
                if (piece == null || piece.isWhite() != forWhite) continue;

                int maxStep = (piece.getType() == Piece.Type.GUARD) ? 1 : piece.getHeight();

                // Try all directions
                for (int[] dir : DIRECTIONS) {
                    for (int step = 1; step <= maxStep; step++) {
                        int newR = r + dir[0] * step;
                        int newC = c + dir[1] * step;

                        if (!isInBounds(newR, newC)) break;

                        // Check path is clear
                        boolean blocked = false;
                        for (int i = 1; i < step; i++) {
                            int midR = r + dir[0] * i;
                            int midC = c + dir[1] * i;
                            if (board[midR][midC] != null) {
                                blocked = true;
                                break;
                            }
                        }
                        if (blocked) break;

                        Piece destination = board[newR][newC];
                        boolean valid = false;

                        if (destination == null) {
                            valid = true; // move to empty
                        } else if (destination.isWhite() == forWhite) {
                            if (piece.getType() == Piece.Type.TOWER && destination.getType() == Piece.Type.TOWER) {
                                valid = true; // stacking
                            }
                        } else {
                            // opponent piece
                            if (piece.getType() == Piece.Type.GUARD) {
                                valid = true; // guard captures anything
                            } else if (destination.getType() == Piece.Type.GUARD) {
                                valid = true; // tower captures guard
                            } else if (destination.getHeight() <= piece.getHeight()) {
                                valid = true; // tower captures same or smaller tower
                            }
                        }

                        if (valid) {
                            moves.add(new Move(r, c, newR, newC, (piece.getType() == Piece.Type.TOWER ? step : 1)));
                        }
                    }
                }
            }
        }
        return moves;
    }

    private static boolean isInBounds(int r, int c) {
        return r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE;
    }
}