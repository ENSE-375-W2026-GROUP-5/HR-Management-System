package hrms.controller;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hrms.db.DatabaseConnection;
import hrms.db.DatabaseInitializer;
import hrms.model.Department;
import hrms.model.EmploymentStatus;
import hrms.model.Role;
import hrms.repository.DepartmentRepository;
import hrms.repository.EmployeeRepository;
import hrms.repository.UserRepository;
import hrms.service.AuthService;
import hrms.service.DepartmentService;
import hrms.service.EmployeeService;

public class AuthControllerTest {

    private AuthController authController;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        clearDatabase();

        DepartmentService departmentService = new DepartmentService(new DepartmentRepository());
        EmployeeService employeeService = new EmployeeService(new EmployeeRepository());
        AuthService authService = new AuthService(new UserRepository());

        authController = new AuthController(authService);

        Department it = departmentService.addDepartment("D001", "IT", "Information Technology");

        employeeService.addEmployee(
                "E001", "John Doe", "john@test.com", "1234567890",
                it, Role.EMPLOYEE, "Developer", 50000, 10,
                EmploymentStatus.ACTIVE, null
        );
    }

    private void clearDatabase() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM leave_requests");
            statement.executeUpdate("DELETE FROM users");
            statement.executeUpdate("DELETE FROM employees");
            statement.executeUpdate("DELETE FROM departments");
        }
    }

    @Test
    void shouldRegisterAndLoginThroughController() {
        authController.registerUser("john", "pass123", "E001", Role.EMPLOYEE);

        assertEquals("john", authController.login("john", "pass123").getUsername());
        assertTrue(authController.hasRole("john", Role.EMPLOYEE));
    }
}