public class Move {
    public final int fromRow, fromCol;
    public final int toRow, toCol;
    public final int moveHeight; // how many pieces are moved (only relevant for towers)

    public Move(int fromRow, int fromCol, int toRow, int toCol, int moveHeight) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.moveHeight = moveHeight;
    }

    public boolean isSameSquare() {
        return fromRow == toRow && fromCol == toCol;
    }

    @Override
    public String toString() {
        return String.format("Move from (%d,%d) to (%d,%d) [%d piece%s]",
                fromRow, fromCol, toRow, toCol, moveHeight, moveHeight == 1 ? "" : "s");
    }

    public String toAlgebraic() {
        return "" + (char) ('a' + fromCol) + (7 - fromRow)
                + (char) ('a' + toCol) + (7 - toRow);
    }

    public Move copy() {
        return new Move(fromRow, fromCol, toRow, toCol, moveHeight);
    }
}