package hrms.service;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
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
import hrms.model.LeaveRequest;
import hrms.model.LeaveStatus;
import hrms.model.LeaveType;
import hrms.model.Role;
import hrms.repository.DepartmentRepository;
import hrms.repository.EmployeeRepository;
import hrms.repository.LeaveRepository;

public class LeaveServiceTest {

    private LeaveService leaveService;
    private EmployeeService employeeService;
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        clearDatabase();

        DepartmentRepository departmentRepository = new DepartmentRepository();
        EmployeeRepository employeeRepository = new EmployeeRepository();
        LeaveRepository leaveRepository = new LeaveRepository();

        departmentService = new DepartmentService(departmentRepository);
        employeeService = new EmployeeService(employeeRepository);
        leaveService = new LeaveService(leaveRepository, employeeRepository);

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
                "E002"
        );

        employeeService.addEmployee(
                "E002",
                "Manager",
                "manager@test.com",
                "9999999999",
                it,
                Role.MANAGER,
                "Manager",
                70000,
                15,
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
    void shouldApplyLeaveSuccessfully() {
        LeaveRequest request = leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.VACATION,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 3),
                3,
                "Trip"
        );

        assertEquals("L001", request.getRequestId());
        assertEquals(LeaveStatus.PENDING, request.getStatus());
    }

    @Test
    void shouldThrowExceptionForDuplicateLeaveRequestId() {
        leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.VACATION,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 3),
                3,
                "Trip"
        );

        assertThrows(IllegalArgumentException.class, () ->
                leaveService.applyLeave(
                        "L001",
                        "E001",
                        LeaveType.SICK,
                        LocalDate.of(2026, 4, 5),
                        LocalDate.of(2026, 4, 6),
                        2,
                        "Fever"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenLeaveExceedsBalance() {
        assertThrows(IllegalArgumentException.class, () ->
                leaveService.applyLeave(
                        "L001",
                        "E001",
                        LeaveType.VACATION,
                        LocalDate.of(2026, 4, 1),
                        LocalDate.of(2026, 4, 20),
                        20,
                        "Long trip"
                )
        );
    }

    @Test
    void shouldApproveLeaveSuccessfully() {
        leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.VACATION,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 3),
                3,
                "Trip"
        );

        leaveService.approveLeave("L001", "E002");

        LeaveRequest request = leaveService.getLeaveRequestById("L001");
        Employee employee = employeeService.getEmployeeById("E001");

        assertEquals(LeaveStatus.APPROVED, request.getStatus());
        assertEquals("E002", request.getApproverId());
        assertEquals(7, employee.getLeaveBalance());
    }

    @Test
    void shouldRejectLeaveSuccessfully() {
        leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.SICK,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 2),
                2,
                "Fever"
        );

        leaveService.rejectLeave("L001", "E002");

        LeaveRequest request = leaveService.getLeaveRequestById("L001");
        assertEquals(LeaveStatus.REJECTED, request.getStatus());
        assertEquals("E002", request.getApproverId());
    }

    @Test
    void shouldCancelPendingLeaveSuccessfully() {
        leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.CASUAL,
                LocalDate.of(2026, 4, 5),
                LocalDate.of(2026, 4, 5),
                1,
                "Personal work"
        );

        leaveService.cancelLeave("L001", "E001");

        LeaveRequest request = leaveService.getLeaveRequestById("L001");
        assertEquals(LeaveStatus.CANCELLED, request.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenUnauthorizedUserApprovesLeave() {
        leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.VACATION,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 3),
                3,
                "Trip"
        );

        assertThrows(IllegalArgumentException.class, () ->
                leaveService.approveLeave("L001", "E001")
        );
    }

    @Test
    void shouldThrowExceptionWhenApprovingNonPendingLeave() {
        leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.VACATION,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 3),
                3,
                "Trip"
        );

        leaveService.approveLeave("L001", "E002");

        assertThrows(IllegalArgumentException.class, () ->
                leaveService.approveLeave("L001", "E002")
        );
    }

    @Test
    void shouldReturnLeaveRequestsByEmployee() {
        leaveService.applyLeave(
                "L001",
                "E001",
                LeaveType.VACATION,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 3),
                3,
                "Trip"
        );

        leaveService.applyLeave(
                "L002",
                "E001",
                LeaveType.CASUAL,
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 10),
                1,
                "Personal"
        );

        List<LeaveRequest> requests = leaveService.getLeaveRequestsByEmployee("E001");
        assertEquals(2, requests.size());
    }
}