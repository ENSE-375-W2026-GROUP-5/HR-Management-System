package hrms.service;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hrms.db.DatabaseConnection;
import hrms.db.DatabaseInitializer;
import hrms.model.Department;
import hrms.model.EmploymentStatus;
import hrms.model.Role;
import hrms.model.UserAccount;
import hrms.repository.DepartmentRepository;
import hrms.repository.EmployeeRepository;
import hrms.repository.UserRepository;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        clearDatabase();

        DepartmentService departmentService = new DepartmentService(new DepartmentRepository());
        EmployeeService employeeService = new EmployeeService(new EmployeeRepository());
        authService = new AuthService(new UserRepository());

        Department it = departmentService.addDepartment("D001", "IT", "Information Technology");

        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                it,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
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
    void shouldRegisterUserSuccessfully() {
        UserAccount user = authService.registerUser("john", "pass123", "E001", Role.EMPLOYEE);

        assertEquals("john", user.getUsername());
        assertEquals(Role.EMPLOYEE, user.getRole());
    }

    @Test
    void shouldThrowExceptionForDuplicateUsername() {
        authService.registerUser("john", "pass123", "E001", Role.EMPLOYEE);

        assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser("john", "newpass", "E001", Role.ADMIN)
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        authService.registerUser("john", "pass123", "E001", Role.EMPLOYEE);

        UserAccount user = authService.login("john", "pass123");

        assertEquals("john", user.getUsername());
        assertEquals("E001", user.getLinkedEmployeeId());
    }

    @Test
    void shouldThrowExceptionForInvalidPassword() {
        authService.registerUser("john", "pass123", "E001", Role.EMPLOYEE);

        assertThrows(IllegalArgumentException.class, () ->
                authService.login("john", "wrongpass")
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.login("ghost", "nopass")
        );
    }

    @Test
    void shouldReturnTrueForCorrectRole() {
        authService.registerUser("john", "pass123", "E001", Role.EMPLOYEE);

        assertTrue(authService.hasRole("john", Role.EMPLOYEE));
    }

    @Test
    void shouldReturnFalseForWrongRole() {
        authService.registerUser("john", "pass123", "E001", Role.EMPLOYEE);

        assertFalse(authService.hasRole("john", Role.ADMIN));
    }
}