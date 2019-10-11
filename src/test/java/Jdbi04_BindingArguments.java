import org.jdbi.v3.core.Jdbi;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jdbi04_BindingArguments {
    private Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");

    @Before
    public void setup() {

    }

    @Test
    public void testIndividualArgumentsBinding() {
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");

            // bind individual arguments
            handle.createUpdate("INSERT INTO user (id, name) VALUES (:id, :name)")
                    .bind("id", 1)
                    .bind("name", "John")
                    .execute();
        });
    }

    @Test
    public void testBindArgumentsFromAMap() {
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");

            // bind arguments from a map
            Map<String, Object> userBindings = new HashMap<>();
            userBindings.put("id", 2);
            userBindings.put("name", "Marry");
            handle.createUpdate("INSERT INTO user (id, name) VALUES (:id, :name)")
                    .bindMap(userBindings)
                    .execute();
        });
    }

    @Test
    public void testBindArgumentsFromAJavaBean() {
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");

            // bind arguments from the properties of a Java bean
            handle.createUpdate("INSERT INTO user (id, name) VALUES (:id, :name)")
                    .bindBean(new User(3, "Victor"))
                    .execute();
        });
    }

    @Test
    public void testBindArgumentsFromAList() {
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");

            // bind arguments from a list
            List<String> names = Arrays.asList("John", "Marry");
            List<User> users = handle.createQuery("SELECT name FROM user WHERE name IN (<listOfNames>)")
                    .bindList("listOfNames", names)
                    .mapToBean(User.class)
                    .list();
        });
    }
}
