import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class Jdbi03_CreateHandlers {
    private Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");

    @Test
    public void testHandlerCallbackWhenReturnsAResult() {
        List<String> names = jdbi.withHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");
            handle.execute("INSERT INTO user (id, name) VALUES (?, ?)", 1, "John");
            return handle.createQuery("SELECT name FROM user").mapTo(String.class).list();
        });

        assertArrayEquals(new String[]{"John"}, names.toArray());
    }

    @Test
    public void testHandlerConsumerWhenResultIsNotNeeded() {
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE product (id INTEGER PRIMARY KEY, name VARCHAR)");
            handle.execute("INSERT INTO product (id, name) VALUES (?, ?)", 1, "Memory");
            List<String> names = handle.createQuery("SELECT name FROM product").mapTo(String.class).list();
            assertArrayEquals(new String[]{"Memory"}, names.toArray());
        });
    }

    @Test
    public void testManualHandler() {
        try (Handle handle = Jdbi.open("jdbc:h2:mem:test")) {
            handle.execute("CREATE TABLE invoice (id INTEGER PRIMARY KEY, number VARCHAR)");
            handle.execute("INSERT INTO invoice (id, number) VALUES (?, ?)", 1, "123321/10");
            List<String> numbers = handle.createQuery("SELECT number FROM invoice").mapTo(String.class).list();
            assertArrayEquals(new String[]{"123321/10"}, numbers.toArray());
        }
    }
}
