import org.apache.commons.collections4.CollectionUtils;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

interface UserDao {
    @SqlUpdate("CREATE TABLE user(id INTEGER PRIMARY KEY, name VARCHAR)")
    void crateTable();

    @SqlUpdate("INSERT INTO user (id, name) VALUES (?, ?)")
    void insertPositional(int id, String name);

    @SqlUpdate("INSERT INTO user (id, name) VALUES (:id, :name)")
    void insertNamed(@Bind("id") int id, @Bind("name") String name);

    @SqlUpdate("INSERT INTO user (id, name) VALUES (:id, :name)")
    void insertBean(@BindBean User user);

    @SqlQuery("SELECT * FROM user ORDER BY name")
    @RegisterBeanMapper(User.class)
    List<User> listAll();
}

public class Jdbi02_DeclarativeApi {
    @Test
    public void testDeclarativeApi() {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());

        List<User> users = jdbi.withExtension(UserDao.class, dao -> {
            dao.crateTable();
            dao.insertPositional(1, "John");
            dao.insertNamed(2, "Anna");
            dao.insertBean(new User(3, "Michael"));
            return dao.listAll();
        });

        assertTrue(CollectionUtils.isEqualCollection(users, Arrays.asList(
                new User(1, "John"),
                new User(2, "Anna"),
                new User(3, "Michael")
        )));
    }
}
