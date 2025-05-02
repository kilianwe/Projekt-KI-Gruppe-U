public class Piece {
    public enum Type {
        GUARD,
        TOWER
    }

    private final Type type;
    private int height; // Only relevant for towers
    private final boolean isRed;

    public Piece(Type type, int height, boolean isRed) {
        this.type = type;
        this.height = (type == Type.GUARD) ? 1 : height;
        this.isRed = isRed;
    }

    public Type getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }

    public boolean isRed() {
        return isRed;
    }

    public void increaseHeight(int by) {
        if (type == Type.TOWER) {
            this.height += by;
        }
    }

    public Piece copy() {
        return new Piece(this.type, this.height, this.isRed);
    }

    @Override
    public String toString() {
        return (isRed ? "R" : "B") + (type == Type.GUARD ? "G" : "T" + height);
    }
}