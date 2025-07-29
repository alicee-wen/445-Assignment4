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
        Position pos = hash(value);
        if(value==null) throw new IllegalArgumentException("Value given was null");
        
        
        int r = pos.row;
        int c = pos.col;

        int[] dims = getDimensions();

        if(r >= dims[0] || c >= dims[1]) throw new IllegalArgumentException("Coordinates out of bounds");

        Cell<T> curr = table[r][c];

        Boolean isFull = checkIfFull(curr);
     
        if(!isFull){
            if(curr==null || !curr.isInSnake) {
                table[r][c] = new Cell<>(value);
            }
            else if(curr.isInSnake){
                curr.value = value;
            }
            return new Position(r, c);
        }
        else{
             if(curr.isInSnake){    //snake probing if given position is part of snake
                return snakeProbe(curr, dims, pos, value);
                }
            else if(!curr.isInSnake){  //if start cell is not in a snake, fall to row probing
                return rowProbe(curr, dims, r, c, value);
            }
        }
        
        throw new IllegalStateException("Table full or no empty cell found");
    }

    @Override
    public Position insert(T value, int row, int col) {
        if(value==null) throw new IllegalArgumentException("Value given was null");
        
        Position pos = new Position(row, col);
        int r = row;
        int c = col;

        int[] dims = getDimensions();

        if(r >= dims[0] || c >= dims[1]) throw new IllegalArgumentException("Coordinates out of bounds");

        Cell<T> curr = table[r][c];

        Boolean isFull = checkIfFull(curr);
     
        if(!isFull){
            if(curr==null || !curr.isInSnake) {
                table[r][c] = new Cell<>(value);
            }
            else if(curr.isInSnake){
                curr.value = value;
            }
            return new Position(r, c);
        }
        else{
             if(curr.isInSnake){    //snake probing if given position is part of snake
                return snakeProbe(curr, dims, pos, value);
                }
            else if(!curr.isInSnake){  //if start cell is not in a snake, fall to row probing
                return rowProbe(curr, dims, r, c, value);
            }
        }
        
        // //reset wasVisited flags
        // for(int i = 0; i < dims[0]; i++){
        //             for(int j = 0; j<dims[1]; j++){
        //                 Cell<T> cell = table[i][j];
        //                 if (cell!= null && cell.wasVisited==true) cell.wasVisited=false;
        //             }
        //         }
        throw new IllegalStateException("Table full or no empty cell found");
    }
 
    public Position snakeProbe(Cell<T> curr, int[] dims, Position originalPos, T value){
        Boolean startFound = false;
        Snake currSnake = curr.snake;
        
        int snakeLength = 0;
        for(Position p: currSnake){
            snakeLength++;
        }

        int lastRow;
        int lastCol;
        int snakeFilled = 0;
        for(Position p: currSnake){
            snakeFilled++;
            if(snakeFilled>snakeLength){ //if snake is full, go to row-probing
                lastRow = p.row;
                lastCol = p.col;
                return rowProbe(curr, dims, p.row, p.col, value);
            }
            if(!startFound){
                if(p.equals(originalPos)){
                    startFound = true;
                }
                continue;
            }
            else if(startFound){ // make sure we're going down the snake, not up the snake
                Cell<T> cell = table[p.row][p.col];
                if(cell.isDeleted || cell.value==null){
                    cell.value = value;
                    cell.wasVisited=true;
                    return new Position(p.row, p.col);
                }
            }
            }
            //if snake is full, go to row probing
            Position last = null;
            for (Position p: currSnake) last = p;

            if(last!=null){
                return rowProbe(table[last.row][last.col], dims, last.row, last.col, value);
            } 
            throw new IllegalStateException("Table full or no empty cell found");
    }


    public Position rowProbe(Cell<T> curr, int[] dims, int r, int c, T value){
        int numRows = dims[0];
        int numCols = dims[1];
        int totalCount = numRows * numCols;

        if(curr==null) {
            table[r][c] = new Cell<>(value);
            curr = table[r][c];
        }
        else{
            for (int i = 0; i < totalCount; i++) {
                int start = r * numCols + c;  // convert 2D coords to integer
                int index = (start + i) % (totalCount); // move next by i steps, wrap around if reached end
                int row = index / numCols; // back to 2D row coord
                int col = index % numCols; // back to 2D col coord


                Cell<T> cell = table[row][col];   
                
                
                // if(cell!= null && cell.wasVisited) throw new IllegalStateException("Table full / no empty cell found");
                // if(cell.isInSnake) {
                //     System.out.println("going to snake probe from rowProbe");
                //     return snakeProbe(cell, dims, new Position(row, col), value);
                // }
            
                if (cell == null || cell.value == null || cell.isDeleted) {
                if (cell == null) table[row][col] = new Cell<>(value);
                else {
                        cell.value = value;
                        cell.isDeleted = false;
                }
                return new Position(row, col);
                }
                cell.wasVisited=true;
        }
    }
    throw new IllegalStateException("Table full / no empty cell found, could not insert value.");
        
}


    @Override
    public boolean deleteKey(T value) {
        if(value==null) throw new IllegalArgumentException("Value given was null");
        int[] dims = getDimensions();

        Position pos = hash(value);
        
        Cell<T> cell = table[pos.row][pos.col];
                if(cell!=null && !cell.isDeleted && (cell.value).equals(value)){
                    cell.value = null;
                    cell.isDeleted = true;
                    return true;
                }
                else return false;
    }

    @Override
    public Position lookupKey(T value) {
        if(value==null) throw new IllegalArgumentException("Value given was null");
        
        Position pos = hash(value);
        Cell<T> cell = table[pos.row][pos.col];
        if(cell!=null && cell.value != null && !cell.isDeleted && (cell.value).equals(value)){
            return pos;
        }
        return null;
    }

    @Override
    public T probe(Position p) {
        if(p == null) throw new IllegalArgumentException("Position given was null");
        int[] dims = getDimensions();
        if(p.row < 0 || p.row >= dims[0] || p.col < 0 || p.col >= dims[1]) throw new IllegalArgumentException("Position given was out of bounds");
        Cell<T> cell = table[p.row][p.col];

        if(cell==null|| cell.value == null || cell.isDeleted) return null;
        else{
            return cell.value;
        }
        
    }

    @Override
    public void addSnake(Snake snake) {
        if(snake==null) throw new IllegalArgumentException("Snake is null");

        int[] dims = getDimensions();
        for (Position pos : snake){
            if(pos.row<0 || pos.col<0 || pos.row>=dims[0] || pos.col>=dims[1]) throw new IllegalArgumentException("Invalid position from snake");
            if(table[pos.row][pos.col]==null) table[pos.row][pos.col] = new Cell<>();
            table[pos.row][pos.col].isInSnake = true;
            table[pos.row][pos.col].snake = snake;
        }
    }

    @Override
    public void removeSnake(Snake snake) {
        if(snake==null) throw new IllegalArgumentException("Snake is null / not found");

        int[] dims = getDimensions();
        for (Position pos : snake){
            if(pos.row<0 || pos.col<0 || pos.row>=dims[0] || pos.col>=dims[1]) throw new IllegalArgumentException("Invalid position from snake to delete");
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


    // public void printTable() {
    //     System.out.println("Table:");
    //     for (int i = 0; i < table.length; i++) {
    //         for (int j = 0; j < table[0].length; j++) {
    //             Cell<T> cell = table[i][j];
    //             String cellStr = ".";
    //             if (cell != null) {
    //                 if (cell.value != null) {
    //                     cellStr = String.valueOf(cell.value);
    //                 }
    //                 if (cell.isDeleted) {
    //                     cellStr = "(" + cellStr + ")";
    //                 } else if (cell.isInSnake) {
    //                     cellStr = "[" + cellStr + "]";
    //                 }
    //             }
    //             System.out.printf("%-8s", cellStr); 
    //         }
    //         System.out.println();
    //     }
    //     System.out.println();
    // }

    

    /**
     * Internal cell wrapper to store value and deletion flag.
     * You may use this class as is or modify its structure.
     */
    private static class Cell<T> {
        T value;
        boolean isDeleted;
        boolean isInSnake;
        boolean wasVisited;
        Snake snake;

        Cell() {
            this.value = null;
            this.isDeleted = false;
            this.isInSnake = false;
            this.wasVisited = false;
        }

        Cell(T value) {
            this.value = value;
            this.isDeleted = false;
            this.isInSnake = false;
            this.wasVisited = false;
        }

        Cell(T value, Snake snake) {
            this.value = value;
            this.isDeleted = false;
            this.isInSnake = false;
            this.snake = snake;
            this.wasVisited = false;
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

