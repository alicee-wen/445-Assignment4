
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SnakeRowProbingTest {

    @Test
    public void testSnakeFirstProbingWithFallbackToRow() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);
        List<Position> snake1 = Arrays.asList(
            new Position(1, 1),
            new Position(1, 2),
            new Position(1, 3));
        table.addSnake(new Snake(snake1));

        table.insert("a", 1, 1); // fills (1,1)
        table.insert("b", 1, 1); // should go to (1,2)
        table.insert("c", 1, 1); // should go to (1,3)
        table.insert("d", 1, 1); // snake full, should go row-wise to (2,0)

        assertEquals("a", table.probe(new Position(1, 1)));
        assertEquals("b", table.probe(new Position(1, 2)));
        assertEquals("c", table.probe(new Position(1, 3)));
        assertEquals("d", table.probe(new Position(2, 0)));
    }

    @Test
    public void testRowFirstProbingThenFollowSnake() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);
        List<Position> snake2 = Arrays.asList(
            new Position(0, 3),
            new Position(1, 3));
        table.addSnake(new Snake(snake2));

        table.insert("e", 0, 2); // fill (0,2)
        table.insert("f", 0, 2); // should probe to (0,3), then (1,3) via snake

        assertEquals("e", table.probe(new Position(0, 2)));
        assertEquals("f", table.probe(new Position(0, 3)));
    }

    @Test
    public void testFullRowMultipleFallbacksAndSnakeFollow() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);
        List<Position> snake3 = Arrays.asList(
            new Position(3, 0),
            new Position(3, 1),
            new Position(3, 2));
        table.addSnake(new Snake(snake3));

        table.insert("g", 2, 2); // (2,2)
        table.insert("h", 2, 2); // (2,3)
        table.insert("i", 2, 2); // wrap to (3,0)
        table.insert("j", 2, 2); // (3,1)
        table.insert("k", 2, 2); // (3,2)

        assertEquals("g", table.probe(new Position(2, 2)));
        assertEquals("h", table.probe(new Position(2, 3)));
        assertEquals("i", table.probe(new Position(3, 0)));
        assertEquals("j", table.probe(new Position(3, 1)));
        assertEquals("k", table.probe(new Position(3, 2)));
    }

    @Test
    public void testIsolatedSnakeProbingFromNonSnake() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);
        List<Position> snake4 = Arrays.asList(
            new Position(3, 0),
            new Position(3, 1),
            new Position(3, 2));
        table.addSnake(new Snake(snake4));

        table.insert("l", 1, 1); // (1,1)
        table.insert("m", 1, 1); // (1,2)
        table.insert("n", 1, 1); // (1,3)
        table.insert("o", 1, 1); // (2,0)
        table.insert("p", 1, 1); // (2,1)
        table.insert("q", 1, 1); // hits (3,0)
        table.insert("r", 1, 1); // (3,1)
        table.insert("s", 1, 1); // (3,2)

        assertEquals("l", table.probe(new Position(1, 1)));
        assertEquals("m", table.probe(new Position(1, 2)));
        assertEquals("n", table.probe(new Position(1, 3)));
        assertEquals("o", table.probe(new Position(2, 0)));
        assertEquals("p", table.probe(new Position(2, 1)));
        assertEquals("q", table.probe(new Position(3, 0)));
        assertEquals("r", table.probe(new Position(3, 1)));
        assertEquals("s", table.probe(new Position(3, 2)));
    }
}
