/**
 * SnakeHashTable is a 2D hashtable data structure that supports O(1) average
 * time insertions, deletions, and lookups using custom probing rules.
 * 
 * Collision resolution is handled via row-wise probing and optionally following
 * custom-defined probe sequences called "snakes".
 *
 * The table stores elements of type T, and each cell may contain at most one
 * value.
 * Snake information is maintained internally.
 *
 * @param <T> the type of elements stored in the hash table
 */
public interface SnakeHashTableInterface<T> {

    /**
     * Inserts the specified value into the hash table using the cutom probing
     * rules.
     *
     * @param value the value to insert
     * @return the final Position in the table where the value was inserted
     * @throws IllegalArgumentException if the value is null
     * @throws IllegalStateException    if the table is full and no empty cell can
     *                                  be found
     */
    public Position insert(T value);

    /**
     * Inserts the specified value into the table, starting at the given (row, col)
     * position.
     * Probing must follow snake links first if any, then row-wise.
     * The method is provided only for testing purposes. Keys inserted using this
     * method cannot be retrieved using lookupKey() or removed using deleteKey().
     *
     * @param value the value to insert
     * @param row   starting row index
     * @param col   starting column index
     * @return the final Position where the value was inserted
     * @throws IllegalArgumentException if the value is null or coordinates are out
     *                                  of bounds
     * @throws IllegalStateException    if the table is full or no empty cell can
     *                                  be found
     */
    public Position insert(T value, int row, int col);

    /**
     * Deletes the specified value from the hash table, if present.
     *
     * @param value the value to delete
     * @return true if the value was found and removed; false otherwise
     * @throws IllegalArgumentException if the value is null
     */
    public boolean deleteKey(T value);

    /**
     * Looks up the given value in the hash table.
     *
     * @param value the value to search for
     * @return the Position of the value if found; null if not found
     * @throws IllegalArgumentException if the value is null
     */
    public Position lookupKey(T value);

    /**
     * Returns the value stored at the given position in the table.
     *
     * @param p the position to probe
     * @return the value at that position, or null if the cell is empty or deleted
     * @throws IllegalArgumentException if the position is null or out of bounds
     */
    public T probe(Position p);

    /**
     * Adds a new snake to the hash table’s probing logic.
     * 
     * Snakes are stored internally and used during probing to define
     * custom probe sequences through the table.
     *
     * @param snake the Snake object to register
     * @throws IllegalArgumentException if the snake is null or its positions are
     *                                  invalid
     */
    public void addSnake(Snake snake);

    /**
     * Removes a previously added snake from the hash table’s probing logic.
     *
     * @param snake the Snake object to remove
     * @throws IllegalArgumentException if the snake is null or not found
     */
    public void removeSnake(Snake snake);

    /**
     * Returns the dimensions of the underlying 2D table.
     *
     * @return an array of two integers: [number of rows, number of columns]
     */
    public int[] getDimensions();
}
