import org.apache.commons.collections4.CollectionUtils;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Jdbi01_FluentApi {

    @Test
    public void testFluentApi() {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");

        List<User> users = jdbi.withHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");

            // inline positional params
            handle.execute("INSERT INTO user (id, name) VALUES (?, ?)", 1, "John");

            // positional params
            handle.createUpdate("INSERT INTO user (id, name) VALUES (?, ?)")
                    .bind(0, 2)
                    .bind(1, "Marry")
                    .execute();

            // named params
            handle.createUpdate("INSERT INTO user (id, name) VALUES (:id, :name)")
                    .bind("id", 3)
                    .bind("name", "Michael")
                    .execute();

            // named params from bean props
            handle.createUpdate("INSERT INTO user (id, name) VALUES (:id, :name)")
                    .bindBean(new User(4, "David"))
                    .execute();

            return handle.createQuery("SELECT * FROM user ORDER BY id")
                    .mapToBean(User.class)
                    .list();
        });

        assertTrue(CollectionUtils.isEqualCollection(users, Arrays.asList(
                new User(1, "John"),
                new User(2, "Marry"),
                new User(3, "Michael"),
                new User(4, "David")
        )));
    }
}
