package hrms.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import hrms.controller.AuthController;
import hrms.controller.EmployeeController;
import hrms.controller.LeaveController;
import hrms.model.Department;
import hrms.model.Employee;
import hrms.model.EmploymentStatus;
import hrms.model.LeaveRequest;
import hrms.model.LeaveType;
import hrms.model.Role;
import hrms.model.UserAccount;
import hrms.service.DepartmentService;

public class CommandLineUI {

    private final AuthController authController;
    private final EmployeeController employeeController;
    private final LeaveController leaveController;
    private final DepartmentService departmentService;
    private final Scanner scanner;

    public CommandLineUI(AuthController authController,
                     EmployeeController employeeController,
                     LeaveController leaveController,
                     DepartmentService departmentService) {
        this.authController = authController;
        this.employeeController = employeeController;
        this.leaveController = leaveController;
        this.departmentService = departmentService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("     Welcome to HR Management System    ");
        System.out.println("========================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void handleLogin() {
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            UserAccount user = authController.login(username, password);
            System.out.println("Login successful. Welcome, " + user.getUsername() + ".");

            if (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER || user.getRole() == Role.HR_MANAGER) {
                adminMenu(user);
            } else {
                employeeMenu(user);
            }

        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void adminMenu(UserAccount user) {
        while (true) {
            System.out.println("\n===== Admin/Manager Menu =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Salary");
            System.out.println("5. Delete Employee");
            System.out.println("6. Apply Leave");
            System.out.println("7. View Leave Requests");
            System.out.println("8. Approve Leave");
            System.out.println("9. Reject Leave");
            System.out.println("10. View by Department");
            System.out.println("11. Logout");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addEmployee();
                        break;
                    case "2":
                        viewAllEmployees();
                        break;
                    case "3":
                        searchEmployee();
                        break;
                    case "4":
                        updateSalary();
                        break;
                    case "5":
                        deleteEmployee();
                        break;
                    case "6":
                        applyLeave();
                        break;
                    case "7":
                        viewLeaveRequests();
                        break;
                    case "8":
                        approveLeave(user.getLinkedEmployeeId());
                        break;
                    case "9":
                        rejectLeave(user.getLinkedEmployeeId());
                        break;
                    case "10":
                        viewByDepartment();
                        break;
                    case "11":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void employeeMenu(UserAccount user) {
        while (true) {
            System.out.println("\n===== Employee Menu =====");
            System.out.println("1. View Profile");
            System.out.println("2. Apply Leave");
            System.out.println("3. View My Leaves");
            System.out.println("4. Cancel Leave");
            System.out.println("5. Logout");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        viewProfile(user.getLinkedEmployeeId());
                        break;
                    case "2":
                        applyLeaveForUser(user.getLinkedEmployeeId());
                        break;
                    case "3":
                        viewMyLeaves(user.getLinkedEmployeeId());
                        break;
                    case "4":
                        cancelLeave(user.getLinkedEmployeeId());
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addEmployee() {
        System.out.println("\nAvailable Departments:");
        List<Department> departments = departmentService.getAllDepartments();

        if (departments.isEmpty()) {
            System.out.println("No departments found. Cannot add employee.");
            return;
        }

        for (Department d : departments) {
            System.out.println(d.getDepartmentId() + " - " + d.getName());
        }

        System.out.print("Employee ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Department ID: ");
        String departmentId = scanner.nextLine().trim();
        Department dept = departmentService.getDepartmentById(departmentId);

        System.out.print("Role (ADMIN, HR_MANAGER, MANAGER, EMPLOYEE): ");
        Role role = Role.valueOf(scanner.nextLine().trim().toUpperCase());

        System.out.print("Designation: ");
        String designation = scanner.nextLine().trim();

        System.out.print("Salary: ");
        double salary = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Leave Balance: ");
        int leaveBalance = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Manager ID (leave blank if none): ");
        String managerId = scanner.nextLine().trim();
        if (managerId.isBlank()) {
            managerId = null;
        }

        Employee employee = employeeController.createEmployee(
                id,
                name,
                email,
                phone,
                dept,
                role,
                designation,
                salary,
                leaveBalance,
                EmploymentStatus.ACTIVE,
                managerId
        );

        System.out.print("Create login account for this employee? (yes/no): ");
        String createLogin = scanner.nextLine().trim().toLowerCase();

        if (createLogin.equals("yes")) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            authController.registerUser(username, password, id, role);
            System.out.println("User account created successfully.");
        }

        System.out.println("\nEmployee added successfully.");
        printEmployeeDetails(employee);
    }

    private void viewAllEmployees() {
        List<Employee> employees = employeeController.getAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("\n================ EMPLOYEES TABLE ================");
        System.out.printf("%-8s %-22s %-15s %-12s %-18s %-10s %-8s%n",
                "ID", "Name", "Dept", "Role", "Designation", "Salary", "Leave");
        System.out.println("--------------------------------------------------------------------------------");

        for (Employee e : employees) {
            System.out.printf("%-8s %-22s %-15s %-12s %-18s %-10.0f %-8d%n",
                    e.getEmployeeId(),
                    e.getFullName(),
                    e.getDepartment().getName(),
                    e.getRole(),
                    e.getDesignation(),
                    e.getSalary(),
                    e.getLeaveBalance());
        }

        System.out.println("--------------------------------------------------------------------------------");
    }

    private void searchEmployee() {
        System.out.print("Employee ID: ");
        String employeeId = scanner.nextLine().trim();

        Employee employee = employeeController.getEmployeeById(employeeId);
        printEmployeeDetails(employee);
    }

    private void updateSalary() {
        System.out.print("Employee ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("New Salary: ");
        double salary = Double.parseDouble(scanner.nextLine().trim());

        employeeController.updateEmployeeSalary(id, salary);
        System.out.println("Updated.");
    }

    private void deleteEmployee() {
        System.out.print("Employee ID: ");
        String id = scanner.nextLine().trim();

        employeeController.deleteEmployee(id);
        System.out.println("Deleted.");
    }

    private void applyLeave() {
        System.out.print("Request ID: ");
        String requestId = scanner.nextLine().trim();

        System.out.print("Employee ID: ");
        String employeeId = scanner.nextLine().trim();

        System.out.print("Leave Type (SICK, CASUAL, VACATION, UNPAID): ");
        LeaveType type = LeaveType.valueOf(scanner.nextLine().trim().toUpperCase());

        System.out.print("Start Date (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("End Date (YYYY-MM-DD): ");
        LocalDate end = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Days: ");
        int days = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Reason: ");
        String reason = scanner.nextLine().trim();

        LeaveRequest request = leaveController.applyLeave(requestId, employeeId, type, start, end, days, reason);
        System.out.println("Leave applied successfully.");
        printLeaveDetails(request);
    }

    private void applyLeaveForUser(String employeeId) {
        System.out.print("Request ID: ");
        String requestId = scanner.nextLine().trim();

        System.out.print("Leave Type (SICK, CASUAL, VACATION, UNPAID): ");
        LeaveType type = LeaveType.valueOf(scanner.nextLine().trim().toUpperCase());

        System.out.print("Start Date (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("End Date (YYYY-MM-DD): ");
        LocalDate end = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Days: ");
        int days = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Reason: ");
        String reason = scanner.nextLine().trim();

        LeaveRequest request = leaveController.applyLeave(requestId, employeeId, type, start, end, days, reason);
        System.out.println("Leave applied successfully.");
        printLeaveDetails(request);
    }

    private void viewLeaveRequests() {
        List<LeaveRequest> requests = leaveController.getAllLeaveRequests();

        if (requests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }

        printLeaveTable(requests);
    }

    private void approveLeave(String approverId) {
        System.out.print("Request ID: ");
        String requestId = scanner.nextLine().trim();

        leaveController.approveLeave(requestId, approverId);
        System.out.println("Approved.");
    }

    private void rejectLeave(String approverId) {
        System.out.print("Request ID: ");
        String requestId = scanner.nextLine().trim();

        leaveController.rejectLeave(requestId, approverId);
        System.out.println("Rejected.");
    }

    private void viewByDepartment() {
        System.out.print("Department ID: ");
        String departmentId = scanner.nextLine().trim();

        List<Employee> employees = employeeController.getEmployeesByDepartment(departmentId);

        if (employees.isEmpty()) {
            System.out.println("No employees found in this department.");
            return;
        }

        System.out.println("\n=========== EMPLOYEES BY DEPARTMENT ==========");
        System.out.printf("%-8s %-22s %-15s %-12s %-18s %-10s %-8s%n",
                "ID", "Name", "Dept", "Role", "Designation", "Salary", "Leave");
        System.out.println("--------------------------------------------------------------------------------");

        for (Employee e : employees) {
            System.out.printf("%-8s %-22s %-15s %-12s %-18s %-10.0f %-8d%n",
                    e.getEmployeeId(),
                    e.getFullName(),
                    e.getDepartment().getName(),
                    e.getRole(),
                    e.getDesignation(),
                    e.getSalary(),
                    e.getLeaveBalance());
        }

        System.out.println("--------------------------------------------------------------------------------");
    }

    private void viewProfile(String employeeId) {
        Employee employee = employeeController.getEmployeeById(employeeId);
        printEmployeeDetails(employee);
    }

    private void viewMyLeaves(String employeeId) {
        List<LeaveRequest> requests = leaveController.getLeaveRequestsByEmployee(employeeId);

        if (requests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }

        printLeaveTable(requests);
    }

    private void cancelLeave(String employeeId) {
        System.out.print("Request ID: ");
        String requestId = scanner.nextLine().trim();

        leaveController.cancelLeave(requestId, employeeId);
        System.out.println("Cancelled.");
    }

    private void printEmployeeDetails(Employee e) {
        System.out.println("\n============== EMPLOYEE DETAILS ==============");
        System.out.println("Employee ID       : " + e.getEmployeeId());
        System.out.println("Full Name         : " + e.getFullName());
        System.out.println("Email             : " + e.getEmail());
        System.out.println("Phone             : " + e.getPhone());
        System.out.println("Department        : " + e.getDepartment().getName());
        System.out.println("Role              : " + e.getRole());
        System.out.println("Designation       : " + e.getDesignation());
        System.out.println("Salary            : $" + String.format("%.2f", e.getSalary()));
        System.out.println("Leave Balance     : " + e.getLeaveBalance());
        System.out.println("Employment Status : " + e.getEmploymentStatus());
        System.out.println("Manager ID        : " + (e.getManagerId() == null ? "N/A" : e.getManagerId()));
        System.out.println("==============================================");
    }

    private void printLeaveDetails(LeaveRequest r) {
        System.out.println("\n============== LEAVE DETAILS =================");
        System.out.println("Request ID    : " + r.getRequestId());
        System.out.println("Employee ID   : " + r.getEmployeeId());
        System.out.println("Leave Type    : " + r.getLeaveType());
        System.out.println("Start Date    : " + r.getStartDate());
        System.out.println("End Date      : " + r.getEndDate());
        System.out.println("Days          : " + r.getNumberOfDays());
        System.out.println("Reason        : " + r.getReason());
        System.out.println("Status        : " + r.getStatus());
        System.out.println("Approver ID   : " + (r.getApproverId() == null ? "N/A" : r.getApproverId()));
        System.out.println("==============================================");
    }

    private void printLeaveTable(List<LeaveRequest> requests) {
        System.out.println("\n================ LEAVE REQUESTS ================");
        System.out.printf("%-10s %-12s %-10s %-12s %-12s %-6s %-12s %-12s%n",
                "Request ID", "Employee ID", "Type", "Start", "End", "Days", "Status", "Approver");
        System.out.println("--------------------------------------------------------------------------------------");

        for (LeaveRequest r : requests) {
            System.out.printf("%-10s %-12s %-10s %-12s %-12s %-6d %-12s %-12s%n",
                    r.getRequestId(),
                    r.getEmployeeId(),
                    r.getLeaveType(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getNumberOfDays(),
                    r.getStatus(),
                    r.getApproverId() == null ? "N/A" : r.getApproverId());
        }

        System.out.println("--------------------------------------------------------------------------------------");
    }
}