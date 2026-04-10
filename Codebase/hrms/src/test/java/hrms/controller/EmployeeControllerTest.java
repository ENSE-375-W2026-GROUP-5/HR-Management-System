package hrms.controller;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hrms.db.DatabaseConnection;
import hrms.db.DatabaseInitializer;
import hrms.model.Department;
import hrms.model.Employee;
import hrms.model.EmploymentStatus;
import hrms.model.Role;
import hrms.repository.DepartmentRepository;
import hrms.repository.EmployeeRepository;
import hrms.service.DepartmentService;
import hrms.service.EmployeeService;

public class EmployeeControllerTest {

    private EmployeeController employeeController;
    private Department department;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        clearDatabase();

        DepartmentService departmentService = new DepartmentService(new DepartmentRepository());
        EmployeeService employeeService = new EmployeeService(new EmployeeRepository());

        employeeController = new EmployeeController(employeeService);
        department = departmentService.addDepartment("D001", "IT", "Information Technology");
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
    void shouldCreateEmployeeThroughController() {
        Employee employee = employeeController.createEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                department,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        assertEquals("E001", employee.getEmployeeId());
        assertEquals("John Doe", employee.getFullName());
    }

    @Test
    void shouldUpdateEmployeeSalaryThroughController() {
        employeeController.createEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                department,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeController.updateEmployeeSalary("E001", 60000);

        assertEquals(60000, employeeController.getEmployeeById("E001").getSalary());
    }

    @Test
    void shouldDeleteEmployeeThroughController() {
        employeeController.createEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                department,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeController.deleteEmployee("E001");

        assertThrows(IllegalArgumentException.class, () ->
                employeeController.getEmployeeById("E001")
        );
    }
}