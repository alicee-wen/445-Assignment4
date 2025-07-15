import java.util.List;
import java.util.Iterator;

public class Snake implements Iterable<Position> {
    private final List<Position> body;

    public Snake(List<Position> body) {
        this.body = body;
    }

    @Override
    public Iterator<Position> iterator() {
        return body.iterator();
    }
}
