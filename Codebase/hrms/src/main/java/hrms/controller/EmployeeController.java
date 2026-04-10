package hrms.controller;

import java.util.List;

import hrms.model.Department;
import hrms.model.Employee;
import hrms.model.EmploymentStatus;
import hrms.model.Role;
import hrms.service.EmployeeService;

/*
 * Controller layer for managing employee-related operations.
 */
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /*
     * Creates a new employee record in the system.
     *
     * @return the created Employee object
     */
    public Employee createEmployee(String employeeId, String fullName, String email, String phone,
                                   Department department, Role role, String designation,
                                   double salary, int leaveBalance,
                                   EmploymentStatus employmentStatus, String managerId) {

        return employeeService.addEmployee(
                employeeId, fullName, email, phone, department, role,
                designation, salary, leaveBalance, employmentStatus, managerId
        );
    }

    /*
     * Retrieves an employee by their unique ID.
     */
    public Employee getEmployeeById(String employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    /*
     * Retrieves all employees in the system.
     */
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /*
     * Updates employee's email address.
     */
    public void updateEmployeeEmail(String employeeId, String newEmail) {
        employeeService.updateEmployeeEmail(employeeId, newEmail);
    }

    /*
     * Updates employee's phone number.
     */
    public void updateEmployeePhone(String employeeId, String newPhone) {
        employeeService.updateEmployeePhone(employeeId, newPhone);
    }

    /*
     * Updates employee's department.
     */
    public void updateEmployeeDepartment(String employeeId, Department newDepartment) {
        employeeService.updateEmployeeDepartment(employeeId, newDepartment);
    }

    /*
     * Updates employee's job designation.
     */
    public void updateEmployeeDesignation(String employeeId, String newDesignation) {
        employeeService.updateEmployeeDesignation(employeeId, newDesignation);
    }

    /*
     * Updates employee's salary.
     */
    public void updateEmployeeSalary(String employeeId, double newSalary) {
        employeeService.updateEmployeeSalary(employeeId, newSalary);
    }

    /*
     * Updates employment status (e.g., ACTIVE, TERMINATED).
     */
    public void updateEmployeeStatus(String employeeId, EmploymentStatus newStatus) {
        employeeService.updateEmployeeStatus(employeeId, newStatus);
    }

    /*
     * Deletes an employee from the system.
     */
    public void deleteEmployee(String employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    /*
     * Retrieves all employees belonging to a specific department.
     *
     * @param departmentId - ID of the department
     * @return list of employees in that department
     */
    public List<Employee> getEmployeesByDepartment(String departmentId) {
        return employeeService.getEmployeesByDepartment(departmentId);
    }
}