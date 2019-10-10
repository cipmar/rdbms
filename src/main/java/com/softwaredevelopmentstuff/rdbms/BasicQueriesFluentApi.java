
package com.softwaredevelopmentstuff.rdbms;

import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class BasicQueriesFluentApi {
    public static void main(String[] args) {
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

            return handle.createQuery("SELECT * FROM user")
                    .mapToBean(User.class)
                    .list();
        });

        users.forEach(System.out::println);
    }
}
