package hrms.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("""
                CREATE TABLE IF NOT EXISTS departments (
                    department_id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    description TEXT
                )
            """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS employees (
                    employee_id TEXT PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    department_id TEXT NOT NULL,
                    role TEXT NOT NULL,
                    designation TEXT NOT NULL,
                    salary REAL NOT NULL,
                    leave_balance INTEGER NOT NULL,
                    employment_status TEXT NOT NULL,
                    manager_id TEXT,
                    FOREIGN KEY (department_id) REFERENCES departments(department_id)
                )
            """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    linked_employee_id TEXT NOT NULL,
                    role TEXT NOT NULL,
                    FOREIGN KEY (linked_employee_id) REFERENCES employees(employee_id)
                )
            """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS leave_requests (
                    request_id TEXT PRIMARY KEY,
                    employee_id TEXT NOT NULL,
                    leave_type TEXT NOT NULL,
                    start_date TEXT NOT NULL,
                    end_date TEXT NOT NULL,
                    number_of_days INTEGER NOT NULL,
                    reason TEXT NOT NULL,
                    status TEXT NOT NULL,
                    approver_id TEXT,
                    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
                )
            """);

        } catch (SQLException e) {
            throw new RuntimeException("Database initialization failed: " + e.getMessage(), e);
        }
    }
}