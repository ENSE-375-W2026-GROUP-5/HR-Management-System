package hrms.app;

import hrms.controller.AuthController;
import hrms.controller.EmployeeController;
import hrms.controller.LeaveController;
import hrms.db.DatabaseInitializer;
import hrms.model.Department;
import hrms.model.EmploymentStatus;
import hrms.model.Role;
import hrms.repository.DepartmentRepository;
import hrms.repository.EmployeeRepository;
import hrms.repository.LeaveRepository;
import hrms.repository.UserRepository;
import hrms.service.AuthService;
import hrms.service.DepartmentService;
import hrms.service.EmployeeService;
import hrms.service.LeaveService;
import hrms.view.CommandLineUI;

/*
 * Main entry point of the HR Management System.
 * Responsible for initializing the database, wiring all layers,
 * adding default, and starting the UI.
 */
public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        // Repository Layer 
        DepartmentRepository departmentRepository = new DepartmentRepository();
        EmployeeRepository employeeRepository = new EmployeeRepository();
        LeaveRepository leaveRepository = new LeaveRepository();
        UserRepository userRepository = new UserRepository();

        // Service Layer 
        DepartmentService departmentService = new DepartmentService(departmentRepository);
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        LeaveService leaveService = new LeaveService(leaveRepository, employeeRepository);
        AuthService authService = new AuthService(userRepository);

        // Controller Layer
        EmployeeController employeeController = new EmployeeController(employeeService);
        LeaveController leaveController = new LeaveController(leaveService);
        AuthController authController = new AuthController(authService);

        seedInitialData(departmentService, employeeService, authService);

        CommandLineUI cli = new CommandLineUI(
                authController,
                employeeController,
                leaveController,
                departmentService
        );

        cli.start();
    }

    /*
     * Adding default data
     * Uses try-catch to avoid duplicate insertion if data already exists.
     */
    private static void seedInitialData(DepartmentService departmentService,
                                        EmployeeService employeeService,
                                        AuthService authService) {

        Department hr;
        Department it;

        try {
            hr = departmentService.getDepartmentById("D001");
        } catch (Exception e) {
            hr = departmentService.addDepartment("D001", "HR", "Human Resources");
        }

        try {
            it = departmentService.getDepartmentById("D002");
        } catch (Exception e) {
            it = departmentService.addDepartment("D002", "IT", "Information Technology");
        }

        try {
            employeeService.getEmployeeById("E001");
        } catch (Exception e) {
            employeeService.addEmployee(
                    "E001",
                    "Ishan Patel",
                    "ishan@company.com",
                    "3061111111",
                    hr,
                    Role.ADMIN,
                    "System Administrator",
                    80000,
                    20,
                    EmploymentStatus.ACTIVE,
                    null
            );
        }

        try {
            employeeService.getEmployeeById("E002");
        } catch (Exception e) {
            employeeService.addEmployee(
                    "E002",
                    "John Doe",
                    "john@company.com",
                    "3062222222",
                    it,
                    Role.MANAGER,
                    "IT Manager",
                    70000,
                    18,
                    EmploymentStatus.ACTIVE,
                    "E001"
            );
        }

        try {
            employeeService.getEmployeeById("E003");
        } catch (Exception e) {
            employeeService.addEmployee(
                    "E003",
                    "Anny Parker",
                    "anny@company.com",
                    "3063333333",
                    it,
                    Role.EMPLOYEE,
                    "Software Developer",
                    55000,
                    12,
                    EmploymentStatus.ACTIVE,
                    "E002"
            );
        }

        try {
            authService.login("Ishan", "admin123");
        } catch (Exception e) {
            authService.registerUser("Ishan", "admin123", "E001", Role.ADMIN);
        }

        try {
            authService.login("John", "manager123");
        } catch (Exception e) {
            authService.registerUser("John", "manager123", "E002", Role.MANAGER);
        }

        try {
            authService.login("Anny", "emp123");
        } catch (Exception e) {
            authService.registerUser("Anny", "emp123", "E003", Role.EMPLOYEE);
        }
    }
}