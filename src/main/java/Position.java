/**
 * Position represents a (row, column) coordinate in a 2D table.
 * It is immutable and can be safely used as a key in hash-based structures.
 */
public class Position {
    public final int row;
    public final int col;

    /**
     * Constructs a Position with the given row and column.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Position))
            return false;
        Position other = (Position) o;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
