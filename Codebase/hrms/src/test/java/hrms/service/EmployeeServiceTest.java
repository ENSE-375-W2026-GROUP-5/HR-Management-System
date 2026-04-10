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
import hrms.model.Employee;
import hrms.model.EmploymentStatus;
import hrms.model.Role;
import hrms.repository.DepartmentRepository;
import hrms.repository.EmployeeRepository;

public class EmployeeServiceTest {

    private EmployeeService employeeService;
    private DepartmentService departmentService;
    private Department itDepartment;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        clearDatabase();

        departmentService = new DepartmentService(new DepartmentRepository());
        employeeService = new EmployeeService(new EmployeeRepository());

        itDepartment = departmentService.addDepartment("D001", "IT", "Information Technology");
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
    void shouldAddEmployeeSuccessfully() {
        Employee employee = employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        assertEquals("E001", employee.getEmployeeId());
        assertEquals("John Doe", employee.getFullName());
        assertEquals("IT", employee.getDepartment().getName());
    }

    @Test
    void shouldThrowExceptionForDuplicateEmployeeId() {
        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        assertThrows(IllegalArgumentException.class, () ->
                employeeService.addEmployee(
                        "E001",
                        "Jane Doe",
                        "jane@test.com",
                        "9999999999",
                        itDepartment,
                        Role.MANAGER,
                        "Lead",
                        70000,
                        15,
                        EmploymentStatus.ACTIVE,
                        null
                )
        );
    }

    @Test
    void shouldGetEmployeeById() {
        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        Employee employee = employeeService.getEmployeeById("E001");

        assertEquals("John Doe", employee.getFullName());
        assertEquals("Developer", employee.getDesignation());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                employeeService.getEmployeeById("E999")
        );
    }

    @Test
    void shouldUpdateEmployeeSalary() {
        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeService.updateEmployeeSalary("E001", 65000);

        Employee updated = employeeService.getEmployeeById("E001");
        assertEquals(65000, updated.getSalary());
    }

    @Test
    void shouldUpdateEmployeeEmail() {
        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeService.updateEmployeeEmail("E001", "newmail@test.com");

        Employee updated = employeeService.getEmployeeById("E001");
        assertEquals("newmail@test.com", updated.getEmail());
    }

    @Test
    void shouldUpdateEmployeePhone() {
        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeService.updateEmployeePhone("E001", "5555555555");

        Employee updated = employeeService.getEmployeeById("E001");
        assertEquals("5555555555", updated.getPhone());
    }

    @Test
    void shouldUpdateEmployeeDepartment() {
        Department hrDepartment = departmentService.addDepartment("D002", "HR", "Human Resources");

        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeService.updateEmployeeDepartment("E001", hrDepartment);

        Employee updated = employeeService.getEmployeeById("E001");
        assertEquals("HR", updated.getDepartment().getName());
    }

    @Test
    void shouldUpdateEmployeeStatus() {
        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeService.updateEmployeeStatus("E001", EmploymentStatus.ON_LEAVE);

        Employee updated = employeeService.getEmployeeById("E001");
        assertEquals(EmploymentStatus.ON_LEAVE, updated.getEmploymentStatus());
    }

    @Test
    void shouldDeleteEmployee() {
        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john@test.com",
                "1234567890",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeService.deleteEmployee("E001");

        assertThrows(IllegalArgumentException.class, () ->
                employeeService.getEmployeeById("E001")
        );
    }

    @Test
    void shouldReturnEmployeesByDepartment() {
        Department hrDepartment = departmentService.addDepartment("D002", "HR", "Human Resources");

        employeeService.addEmployee(
                "E001",
                "John Doe",
                "john1@test.com",
                "1111111111",
                itDepartment,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                null
        );

        employeeService.addEmployee(
                "E002",
                "Jane Doe",
                "john2@test.com",
                "2222222222",
                hrDepartment,
                Role.EMPLOYEE,
                "HR Associate",
                48000,
                12,
                EmploymentStatus.ACTIVE,
                null
        );

        List<Employee> itEmployees = employeeService.getEmployeesByDepartment("D001");

        assertEquals(1, itEmployees.size());
        assertEquals("E001", itEmployees.get(0).getEmployeeId());
    }
}