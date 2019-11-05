package mysql;

import org.jdbi.v3.core.Jdbi;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class SimpleSelect {

    @Test
    public void selectEmployees() throws Exception {
        String url = "jdbc:mysql://localhost/test";
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection(url, "root", "password");
        Jdbi jdbi = Jdbi.create(conn);

        List<Employee> employees = jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM employees").mapToBean(Employee.class).list());

        employees.forEach(System.out::println);
    }
}
