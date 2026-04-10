package hrms.integration;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hrms.db.DatabaseConnection;
import hrms.db.DatabaseInitializer;
import hrms.model.Department;
import hrms.model.Employee;
import hrms.model.EmploymentStatus;
import hrms.model.LeaveRequest;
import hrms.model.LeaveStatus;
import hrms.model.LeaveType;
import hrms.model.Role;
import hrms.repository.DepartmentRepository;
import hrms.repository.EmployeeRepository;
import hrms.repository.LeaveRepository;
import hrms.repository.UserRepository;
import hrms.service.AuthService;
import hrms.service.DepartmentService;
import hrms.service.EmployeeService;
import hrms.service.LeaveService;

public class HRSystemTest {

    private DepartmentService departmentService;
    private EmployeeService employeeService;
    private LeaveService leaveService;
    private AuthService authService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        clearDatabase();

        departmentService = new DepartmentService(new DepartmentRepository());
        employeeService = new EmployeeService(new EmployeeRepository());
        leaveService = new LeaveService(new LeaveRepository(), new EmployeeRepository());
        authService = new AuthService(new UserRepository());
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
    void shouldCompleteEmployeeLeaveWorkflow() {
        Department it = departmentService.addDepartment("D001", "IT", "Information Technology");

        Employee manager = employeeService.addEmployee(
                "E001",
                "Manager",
                "manager@test.com",
                "1111111111",
                it,
                Role.MANAGER,
                "Team Lead",
                70000,
                15,
                EmploymentStatus.ACTIVE,
                null
        );

        Employee employee = employeeService.addEmployee(
                "E002",
                "John Doe",
                "john@test.com",
                "2222222222",
                it,
                Role.EMPLOYEE,
                "Developer",
                50000,
                10,
                EmploymentStatus.ACTIVE,
                "E001"
        );

        authService.registerUser("manager1", "pass123", "E001", Role.MANAGER);
        authService.registerUser("john1", "pass123", "E002", Role.EMPLOYEE);

        LeaveRequest request = leaveService.applyLeave(
                "L001",
                "E002",
                LeaveType.VACATION,
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 12),
                3,
                "Trip"
        );

        leaveService.approveLeave("L001", "E001");

        Employee updatedEmployee = employeeService.getEmployeeById("E002");
        LeaveRequest updatedRequest = leaveService.getLeaveRequestById("L001");

        assertEquals(LeaveStatus.APPROVED, updatedRequest.getStatus());
        assertEquals(7, updatedEmployee.getLeaveBalance());
        assertEquals("IT", updatedEmployee.getDepartment().getName());
        assertEquals("E001", manager.getEmployeeId());
        assertEquals("E002", employee.getEmployeeId());
        assertEquals("E001", updatedRequest.getApproverId());
        assertEquals("manager1", authService.login("manager1", "pass123").getUsername());
    }
}