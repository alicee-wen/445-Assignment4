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
          System.out.println("NEW ITERATION");
          printTable();
  
          Position pos = hash(value);
          int r = pos.row;
          int c = pos.col;
  
          int[] dims = getDimensions();
  
          Cell<T> curr = table[r][c];
          System.out.println("value : " + value);
          // System.out.println("curr.isinsnake = " + curr.isInSnake);
  
          Boolean isFull = checkIfFull(curr);
       
          if(!isFull){
              // // Boolean wasInSnake = curr.isInSnake;
              // table[r][c] = new Cell<>(value);
              if(curr==null) table[r][c] = new Cell<>(value);
              else curr.value = value;
      
              // if(wasInSnake) 
              System.out.println("was not full");
              return new Position(r, c);
          }
          else{
  
              //snake probing
               //snake probing
               if(curr.isInSnake){
                  Boolean startFound = false;
                  Snake currSnake = table[r][c].snake;
                  
                  for(Position p: currSnake){
                      if(!startFound){
                          if(p.equals(pos)){
                              startFound = true;
                          }
                          continue;
                      }
                      else if(startFound){
                          Cell<T> cell = table[p.row][p.col];
                          if(cell.isDeleted || cell.value==null){
                              cell.value = value;
                              System.out.println("after snake probe insert");
  
                              printTable();
                              return new Position(p.row, p.col);
                          }
                      }
                          
                      }
                  }
              //row probing
              else if(!curr.isInSnake){
                  while(!curr.isInSnake && r < dims[0] && c < dims[1]){
                      if(c+1 == dims[1]){
                          curr = table[r+1][c];
                      }
                      if(r+1==dims[0] && c+1==dims[1]){
                          curr = table[0][0];
                      curr=table[r][c+1];
                  }
                  curr.value = value;
                  System.out.println("after row insert");
                  printTable();
                  return lookupKey(value);
              }
          }
          System.out.println("neither");
          printTable();
          return null;
    }
    }

    @Override
    public Position insert(T value, int row, int col) {
        // TODO: Perform probing starting at this position (reuse same strategy)
        if(value==null) throw new IllegalArgumentException("Value given was null");
        System.out.println("NEW ITERATION");
        System.out.println("MANUAL INSERT AT ROW: " + row + "COL: " + col);
        System.out.println("VALUE: " + value);
        System.out.println("before insertion: ");
        printTable();

        Position pos = new Position(row, col);
        int r = row;
        int c = col;

        int[] dims = getDimensions();

        if(r >= dims[0] || c >= dims[1]) throw new IllegalArgumentException("Coordinates out of bounds");

        Cell<T> curr = table[r][c];

        Boolean isCurrNull;
        if(curr==null) isCurrNull=true;
        else isCurrNull = false;

        System.out.println("Is curr null? " + isCurrNull);
        if(curr!=null) System.out.println("curr.isinsnake = " + (curr.isInSnake));

        Boolean isFull = checkIfFull(curr);
     
        if(!isFull){
            if(curr==null || !curr.isInSnake) {
                table[r][c] = new Cell<>(value);
                System.out.println("curr was null or was not in snake");
            }
            else if(curr.isInSnake){
                curr.value = value;
                System.out.println("curr was in snake");
            }
 
            System.out.println("was not full");
            System.out.println("after insert at position " + r + ", " + c);
            printTable();
            return new Position(r, c);
        }
        else{

            //snake probing
             //snake probing
             if(curr.isInSnake){
                return snakeProbe(curr, dims, pos, value);
                }
            //if start cell is not in a snake, fall to row probing
            else if(!curr.isInSnake){
                return rowProbe(curr, dims, r, c, value);
            }
        }
        
        //reset wasVisited flags
        for(int i = 0; i < dims[0]; i++){
                    for(int j = 0; j<dims[1]; j++){
                        Cell<T> cell = table[i][j];
                        if (cell!= null && cell.wasVisited==true) cell.wasVisited=false;
                    }
                }
        return null;
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
            if(snakeFilled>snakeLength){
                lastRow = p.row;
                lastCol = p.col;
                System.out.println("snake full");
                return rowProbe(curr, dims, p.row, p.col, value);
            }
            if(!startFound){
                if(p.equals(originalPos)){
                    startFound = true;
                }
                continue;
            }
            else if(startFound){
                Cell<T> cell = table[p.row][p.col];
                if(cell.isDeleted || cell.value==null){
                    cell.value = value;
                    cell.wasVisited=true;
                    System.out.println("after snake probe insert");

                    printTable();
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
            return null;
    }
    public Position rowProbe(Cell<T> curr, int[] dims, int r, int c, T value){
        System.out.println("in rowProbe method");
        System.out.println("row: " + r + "col: " + c);
    
        int totalCount = dims[0] * dims[1];

        if(curr==null) {
            table[r][c] = new Cell<>(value);
            curr = table[r][c];
        }
        else{
            
            for (int i = 0; i < totalCount; i++) {
                System.out.println("in for loop in rowProbe");
                int row = (r + (c + i) / dims[1]) % dims[0];
                int col = (c + i) % dims[1];
                Cell<T> cell = table[row][col];
                System.out.println("grabbed cell at row " + row + ", col " + col);
                
                
                
                // if(cell!= null && cell.wasVisited) throw new IllegalStateException("Table full / no empty cell found");
                // if(cell.isInSnake) {
                //     System.out.println("going to snake probe from rowProbe");
                //     return snakeProbe(cell, dims, new Position(row, col), value);
                // }
                if(cell==null) System.out.println("Cell is null");
                else System.out.println("Cell is not null");
               
                if (cell == null || cell.value == null || cell.isDeleted) {
                if (cell == null) table[row][col] = new Cell<>(value);
                else {
                        cell.value = value;
                        cell.isDeleted = false;
                }
                System.out.println("Inserted by rowProbe at: (" + row + "," + col + ")");
                printTable();
                Position insertedAt = new Position(row, col);
                return new Position(row, col);
                }
                cell.wasVisited=true;
                System.out.println("cell inserted: value = " + value + "is cell visited?" + cell.wasVisited);
            
    
        }
    }
        throw new IllegalStateException("Table full / no empty cell found, could not insert value.");
        
}

// public Position lookupKeyFromManualInsert(T value){
//     if(value==null) throw new IllegalArgumentException("Value given was null");

//     int[] dims = getDimensions();
//     for(int r = 0; r < dims[0]; r++){
//         for(int c = 0; c<dims[1]; c++){
//             Cell<T> curr = table[r][c];
//             if (curr!= null && !curr.isDeleted && curr.value.equals(value)) return new Position(r, c);
//         }
//     }
//     return null;
// }

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
        // TODO: Implement probing logic with bounds checking
        if(p == null) throw new IllegalArgumentException("Position given was null");
        int[] dims = getDimensions();
        if(p.row >= dims[0] || p.col >= dims[1]) throw new IllegalArgumentException("Position given was out of bounds");
        Cell<T> cell = table[p.row][p.col];


        // System.out.println(cell.isDeleted);
        // System.out.println("cell value: " + cell.value);
        if(cell==null|| cell.value == null || cell.isDeleted) return null;
        else{
            return cell.value;
        }
        
    }

    @Override
    public void addSnake(Snake snake) {
        if(snake==null) throw new IllegalArgumentException("Snake is null");
        System.out.println("Before add Snake");
        System.out.println("Snake: ");
        for (Position p : snake){
            System.out.println(p);
        }
        printTable();


        int[] dims = getDimensions();
        for (Position pos : snake){
            if(pos.row<0 || pos.col<0 || pos.row>=dims[0] || pos.col>=dims[1]) throw new IllegalArgumentException("Invalid position");
            if(table[pos.row][pos.col]==null) table[pos.row][pos.col] = new Cell<>();
            table[pos.row][pos.col].isInSnake = true;
            table[pos.row][pos.col].snake = snake;
        }
        System.out.println("After addsnake: ");
        printTable();

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
        return null;
    }


    public void printTable() {
        System.out.println("Table:");
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                Cell<T> cell = table[i][j];
                String cellStr = ".";
                if (cell != null) {
                    if (cell.value != null) {
                        cellStr = String.valueOf(cell.value);
                    }
                    if (cell.isDeleted) {
                        cellStr = "(" + cellStr + ")";
                    } else if (cell.isInSnake) {
                        cellStr = "[" + cellStr + "]";
                    }
                }
                System.out.printf("%-8s", cellStr); // left-aligned 8-width cell
            }
            System.out.println();
        }
        System.out.println();
    }

    

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

