package hrms.service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hrms.db.DatabaseConnection;
import hrms.db.DatabaseInitializer;
import hrms.model.Department;
import hrms.repository.DepartmentRepository;

public class DepartmentServiceTest {

    private DepartmentService departmentService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        clearDatabase();
        departmentService = new DepartmentService(new DepartmentRepository());
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
    void shouldAddDepartmentSuccessfully() {
        Department department = departmentService.addDepartment("D001", "IT", "Information Technology");

        assertEquals("D001", department.getDepartmentId());
        assertEquals("IT", department.getName());
    }

    @Test
    void shouldThrowExceptionForDuplicateDepartmentId() {
        departmentService.addDepartment("D001", "IT", "Information Technology");

        assertThrows(IllegalArgumentException.class, () ->
                departmentService.addDepartment("D001", "HR", "Human Resources")
        );
    }

    @Test
    void shouldGetDepartmentById() {
        departmentService.addDepartment("D001", "IT", "Information Technology");

        Department department = departmentService.getDepartmentById("D001");

        assertEquals("IT", department.getName());
    }

    @Test
    void shouldThrowExceptionWhenDepartmentNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                departmentService.getDepartmentById("D999")
        );
    }

    @Test
    void shouldReturnAllDepartments() {
        departmentService.addDepartment("D001", "IT", "Information Technology");
        departmentService.addDepartment("D002", "HR", "Human Resources");

        List<Department> departments = departmentService.getAllDepartments();

        assertEquals(2, departments.size());
    }

    @Test
    void shouldDeleteDepartmentSuccessfully() {
        departmentService.addDepartment("D001", "IT", "Information Technology");

        departmentService.deleteDepartment("D001");

        assertThrows(IllegalArgumentException.class, () ->
                departmentService.getDepartmentById("D001")
        );
    }
}