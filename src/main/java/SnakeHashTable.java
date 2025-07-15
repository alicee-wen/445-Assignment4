/**
 * Skeleton implementation of SnakeHashTableInterface.
 * 
 * @param <T> the type of values stored in the table
 */
public class SnakeHashTable<T> implements SnakeHashTableInterface<T> {

    // 🚫 DO NOT ADD ANY OTHER INSTANCE VARIABLES!
    private final Cell<T>[][] table;

    /**
     * Constructs a new SnakeHashTable with the specified number of rows and
     * columns.
     *
     * @param rows number of rows in the table
     * @param cols number of columns in the table
     */
    @SuppressWarnings("unchecked")
    public SnakeHashTable(int rows, int cols) {
        table = (Cell<T>[][]) new Cell[rows][cols];
    }

    @Override
    public Position insert(T value) {
        // TODO: Implement insert logic using snake and row-wise probing
        return null;
    }

    @Override
    public Position insert(T value, int row, int col) {
        // TODO: Perform probing starting at this position (reuse same strategy)
        return null;
    }

    @Override
    public boolean deleteKey(T value) {
        // TODO: Implement delete logic
        return false;
    }

    @Override
    public Position lookupKey(T value) {
        // TODO: Implement lookup logic
        return null;
    }

    @Override
    public T probe(Position p) {
        // TODO: Implement probing logic with bounds checking
        return null;
    }

    @Override
    public void addSnake(Snake snake) {
        // TODO: Implement snake registration
    }

    @Override
    public void removeSnake(Snake snake) {
        // TODO: Implement snake removal
    }

    @Override
    public int[] getDimensions() {
        // TODO: Implement the method
        return null;
    }

    /**
     * Internal cell wrapper to store value and deletion flag.
     * You may use this class as is or modify its structure.
     */
    private static class Cell<T> {
        T value;
        boolean isDeleted;

        Cell(T value) {
            this.value = value;
            this.isDeleted = false;
        }
    }

    /**
     * Computes the hash position of the given key in the table.
     * You must use this method in insert, lookup, and delete.
     *
     * @param key the value to hash
     * @return a Position within the table bounds
     */
    private Position hash(T key) {
        int h = Math.abs(key.hashCode());
        int rows = table.length;
        int cols = table[0].length;
        int row = h % rows;
        int col = (h / rows) % cols;
        return new Position(row, col);
    }
}
