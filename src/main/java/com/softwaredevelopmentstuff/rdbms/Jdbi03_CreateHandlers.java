package com.softwaredevelopmentstuff.rdbms;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class Jdbi03_CreateHandlers {
    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");

        // callback, when operation returns a result
        List<String> names = jdbi.withHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");
            handle.execute("INSERT INTO user (id, name) VALUES (?, ?)", 1, "John");
            return handle.createQuery("SELECT name FROM user").mapTo(String.class).list();
        });

        System.out.println(names);

        // consumer, when operation doesn't return an result
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE product (id INTEGER PRIMARY KEY, name VARCHAR)");
            handle.execute("INSERT INTO product (id, name) VALUES (?, ?)", 1, "Memory");
            List<String> names2 = handle.createQuery("SELECT name FROM product").mapTo(String.class).list();
            System.out.println(names2);
        });

        // manually open connection
        try (Handle handle = Jdbi.open("jdbc:h2:mem:test")) {
            handle.execute("CREATE TABLE invoice (id INTEGER PRIMARY KEY, number VARCHAR)");
            handle.execute("INSERT INTO invoice (id, number) VALUES (?, ?)", 1, "123321/10");
            List<String> names3 = handle.createQuery("SELECT number FROM invoice").mapTo(String.class).list();
            System.out.println(names3);
        }
    }
}
