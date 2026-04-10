package hrms.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeTest {

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department("D001", "IT", "Information Technology");
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        Employee employee = new Employee(
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
        assertEquals("Developer", employee.getDesignation());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("", "John Doe", "john@test.com", "1234567890",
                        department, Role.EMPLOYEE, "Developer", 50000, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenFullNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "", "john@test.com", "1234567890",
                        department, Role.EMPLOYEE, "Developer", 50000, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "", "1234567890",
                        department, Role.EMPLOYEE, "Developer", 50000, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "john@test.com", "",
                        department, Role.EMPLOYEE, "Developer", 50000, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenDepartmentIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "john@test.com", "1234567890",
                        null, Role.EMPLOYEE, "Developer", 50000, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "john@test.com", "1234567890",
                        department, null, "Developer", 50000, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenDesignationIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "john@test.com", "1234567890",
                        department, Role.EMPLOYEE, "", 50000, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenSalaryIsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "john@test.com", "1234567890",
                        department, Role.EMPLOYEE, "Developer", -1, 10,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenLeaveBalanceIsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "john@test.com", "1234567890",
                        department, Role.EMPLOYEE, "Developer", 50000, -1,
                        EmploymentStatus.ACTIVE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmploymentStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Employee("E001", "John Doe", "john@test.com", "1234567890",
                        department, Role.EMPLOYEE, "Developer", 50000, 10,
                        null, null)
        );
    }

    @Test
    void shouldUpdateEmployeeFields() {
        Employee employee = new Employee(
                "E001", "John Doe", "john@test.com", "1234567890",
                department, Role.EMPLOYEE, "Developer", 50000, 10,
                EmploymentStatus.ACTIVE, null
        );

        Department hr = new Department("D002", "HR", "Human Resources");

        employee.setEmail("new@test.com");
        employee.setPhone("9999999999");
        employee.setDepartment(hr);
        employee.setDesignation("Senior Developer");
        employee.setSalary(60000);
        employee.setLeaveBalance(15);
        employee.setEmploymentStatus(EmploymentStatus.ON_LEAVE);

        assertEquals("new@test.com", employee.getEmail());
        assertEquals("9999999999", employee.getPhone());
        assertEquals("HR", employee.getDepartment().getName());
        assertEquals("Senior Developer", employee.getDesignation());
        assertEquals(60000, employee.getSalary());
        assertEquals(15, employee.getLeaveBalance());
        assertEquals(EmploymentStatus.ON_LEAVE, employee.getEmploymentStatus());
    }
}