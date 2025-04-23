public class Piece {
    public enum Type {
        GUARD,
        TOWER
    }

    private final Type type;
    private int height; // Only relevant for towers
    private final boolean isWhite;

    public Piece(Type type, int height, boolean isWhite) {
        this.type = type;
        this.height = (type == Type.GUARD) ? 1 : height;
        this.isWhite = isWhite;
    }

    public Type getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void increaseHeight(int by) {
        if (type == Type.TOWER) {
            this.height += by;
        }
    }

    public Piece copy() {
        return new Piece(this.type, this.height, this.isWhite);
    }

    @Override
    public String toString() {
        return (isWhite ? "W" : "B") + (type == Type.GUARD ? "G" : "T" + height);
    }
}
