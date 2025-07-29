import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SnakeRowToSnakeProbingTest {

    @Test
    public void testRowFirstThenSnakeProbing() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);
        
        // Create a snake from (0,3) to (1,3)
        List<Position> snake = Arrays.asList(
            new Position(0, 3),
            new Position(1, 3)
        );
        table.addSnake(new Snake(snake));

        // Force (0,2) to be filled
        table.insert("X", 0, 2);  // Fill initial cell not in a snake
        table.insert("Y", 0, 2);  // Should go to (0,3)
        table.insert("Z", 0, 2);  // Should go to (1,3)

        assertEquals("X", table.probe(new Position(0, 2)));
        assertEquals("Y", table.probe(new Position(0, 3)));
        assertEquals("Z", table.probe(new Position(1, 3)));
    }
}