import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 * JUnit 4 tests for the SnakeHashTable implementation.
 * Tests are grouped into phases: correctness, probing, and snake logic
 */
public class SnakeHashTableTest {

    // ----------------------------
    // Phase 1: Core Functionality
    // ----------------------------

    @Test
    public void testHashInsertLookupDelete() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);

        Position pos = table.insert("apple");
        assertNotNull("Insert should return a position", pos);

        Position lookup = table.lookupKey("apple");
        assertEquals("Lookup should return the same position", pos, lookup);

        assertTrue("Delete should return true for found key", table.deleteKey("apple"));
        assertNull("Lookup should return null after deletion", table.lookupKey("apple"));
    }

    @Test
    public void testDimensions() {
        SnakeHashTable<String> table = new SnakeHashTable<>(5, 6);
        int[] dims = table.getDimensions();
        assertArrayEquals("Dimensions should match constructor args", new int[] { 5, 6 }, dims);
    }

    // ----------------------------
    // Phase 2: Probing Behavior
    // ----------------------------

    @Test
    public void testProbeAfterDirectInsert() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);
        Position expected = new Position(2, 2);
        Position actual = table.insert("banana", 2, 2);
        assertEquals("Manual insert should return the correct position", expected, actual);
        assertEquals("Probe should return the inserted value", "banana", table.probe(expected));
    }

    @Test
    public void testLookupAndDeleteIgnoreManualInsert() {
        SnakeHashTable<String> table = new SnakeHashTable<>(3, 3);

        // Insert manually bypassing the hash function
        Position manualPos = table.insert("manual", 2, 2);
        assertEquals("manual", table.probe(manualPos));
    }

    // ----------------------------
    // Phase 3: Snake-Based Probing
    // ----------------------------

    @Test
    public void testSnakeProbingSequence() {
        SnakeHashTable<String> table = new SnakeHashTable<>(3, 3);

        List<Position> body = Arrays.asList(
                new Position(0, 1),
                new Position(1, 1),
                new Position(2, 1));
        Snake snake = new Snake(body);
        table.addSnake(snake);

        table.insert("x", 0, 1); // should go to (0,1)
        table.insert("y", 0, 1); // follow to (1,1)
        table.insert("z", 0, 1); // follow to (2,1)

        assertEquals("x", table.probe(new Position(0, 1)));
        assertEquals("y", table.probe(new Position(1, 1)));
        assertEquals("z", table.probe(new Position(2, 1)));
    }


    @Test
    public void testKeyInsertedManuallyNotFoundViaHash() {
        SnakeHashTable<String> table = new SnakeHashTable<>(4, 4);

        // Define a snake that does NOT include (0, 0)
        List<Position> snakeBody = Arrays.asList(
                new Position(1, 1),
                new Position(2, 1),
                new Position(3, 1));
        Snake snake = new Snake(snakeBody);
        table.addSnake(snake);

        // Find a string that hashes to one of the snake's cells
        String ghostKey = findKeyThatHashesToOneOf(table, snakeBody);
        assertNotNull("Precondition failed: could not find hash-matching key", ghostKey);

        // Insert the same key manually at (0, 0), which is not part of the snake
        table.insert(ghostKey, 0, 0);

        // Now try to look it up using the hash-based lookup
        assertNull("lookupKey should return null since key is not reachable from its hashed position",
                table.lookupKey(ghostKey));

        //Now insert the same key using hash-based insert and look it up      
        Position pos = table.insert(ghostKey);
        assertNotNull(pos);

        Position lookup = table.lookupKey(ghostKey);
        assertEquals("Lookup should return the same position", pos, lookup);

    }

    // ----------------------------
    // Helper Methods
    // ----------------------------


    private String findKeyThatHashesToOneOf(SnakeHashTable<String> table, List<Position> targets) {
        int i = 0;
        while (true) {
            String candidate = "ghost" + i++;
            Position hashPos = invokeHash(table, candidate);
            if (targets.contains(hashPos)) {
                return candidate;
            }
            if (i > 10000)
                return null; // give up to avoid infinite loop
        }
    }

    /**
     * Reflectively call the private hash(T) method.
     * Used for testing only. Assumes hash is deterministic.
     */
    @SuppressWarnings("unchecked")
    private Position invokeHash(SnakeHashTable<String> table, String key) {
        try {
            java.lang.reflect.Method m = table.getClass().getDeclaredMethod("hash", Object.class);
            m.setAccessible(true);
            return (Position) m.invoke(table, key);
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke hash() for testing", e);
        }
    }

}
