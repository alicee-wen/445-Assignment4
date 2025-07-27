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
        if(value==null) throw new IllegalArgumentException("Value given was null");
        System.out.println("value: " + value);
        Position pos = hash(value);

        Cell<T> curr = table[pos.row][pos.col];

        Boolean isFull = checkIfFull(curr);

        if(!isFull){
            table[pos.row][pos.col] = new Cell<>(value);
            return new Position(pos.row, pos.col);
        }
        else{

            if(curr.isInSnake){
                Boolean startFound = false;
                Snake currSnake = table[pos.row][pos.col].snake;
                Position insertAt;
                for(Position p : currSnake){
                    if(p.equals(pos)){
                        startFound = true;
                        insertAt = p;
                        break;
                        
                    }
                }
            }
            if(!curr.isInSnake){
                while(!curr.isInSnake && pos.row < dim)
            }
        }
        
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
        if(p == null) throw new IllegalArgumentException("Position given was null");
        int[] dims = getDimensions();
        if(p.row >= dims[0] || p.col >= dims[1]) throw new IllegalArgumentException("Position given was out of bounds");
        Cell<T> cell = table[p.row][p.col];

        // System.out.println(cell.isDeleted);
        // System.out.println("cell value: " + cell.value);
        if(cell.value == null || cell.isDeleted) return null;
        else{
            return cell.value;
        }
        
    }

    @Override
    public void addSnake(Snake snake) {
        if(snake==null) throw new IllegalArgumentException("Snake is null");

        int[] dims = getDimensions();
        for (Position pos : snake){
            if(pos.row<0 || pos.col<0 || pos.row>=dims[0] || pos.col>=dims[1]) throw new IllegalArgumentException("Invalid position");
            if(table[pos.row][pos.col]==null) table[pos.row][pos.col] = new Cell<>();
            table[pos.row][pos.col].isInSnake = true;
            table[pos.row][pos.col].snake = snake;
        }
    }

    @Override
    public void removeSnake(Snake snake) {
        if(snake==null) throw new IllegalArgumentException("Snake is null");

        int[] dims = getDimensions();
        for (Position pos : snake){
            if(pos.row<0 || pos.col<0 || pos.row>=dims[0] || pos.col>=dims[1]) throw new IllegalArgumentException("Invalid position");
            if(!table[pos.row][pos.col].isDeleted){
                table[pos.row][pos.col].isDeleted = true;
                table[pos.row][pos.col].isInSnake = false;

            }
        }
    }

    @Override
    public int[] getDimensions() {
        int[] dimensions = new int[2];
        dimensions[0] = table.length;
        dimensions[1] = table[0].length;
        return dimensions;
    }

    public boolean checkIfFull(Cell<T> cell){
        return (cell!=null && !cell.isDeleted && cell.value!=null);
    }

    public Snake getSnakeFromCell(Cell<T> cell){

    }

    /**
     * Internal cell wrapper to store value and deletion flag.
     * You may use this class as is or modify its structure.
     */
    private static class Cell<T> {
        T value;
        boolean isDeleted;
        boolean isInSnake;
        Snake snake;

        Cell() {
            this.value = null;
            this.isDeleted = false;
            this.isInSnake = false;
        }

        Cell(T value) {
            this.value = value;
            this.isDeleted = false;
            this.isInSnake = false;
        }

        Cell(T value, Snake snake) {
            this.value = value;
            this.isDeleted = false;
            this.isInSnake = false;
            this.snake = snake;
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

