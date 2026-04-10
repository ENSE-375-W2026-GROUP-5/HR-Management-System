package hrms.service;

import java.util.ArrayList;
import java.util.List;

import hrms.model.Department;
import hrms.model.Employee;
import hrms.model.EmploymentStatus;
import hrms.model.Role;
import hrms.repository.EmployeeRepository;

/*
 * Service layer for handling employee-related business logic.
 */
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /*
     * Adds a new employee to the system.
     *
     * @param employeeId - unique employee ID
     * @param fullName - employee's full name
     * @param email - employee's email
     * @param phone - employee's phone number
     * @param department - employee's department
     * @param role - employee's role
     * @param designation - employee's job title
     * @param salary - employee's salary
     * @param leaveBalance - available leave balance
     * @param employmentStatus - current employment status
     * @param managerId - ID of the employee's manager
     * @return created Employee object
     */
    public Employee addEmployee(String employeeId, String fullName, String email, String phone,
                                Department department, Role role, String designation,
                                double salary, int leaveBalance,
                                EmploymentStatus employmentStatus, String managerId) {

        if (employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Employee ID already exists.");
        }

        Employee employee = new Employee(
                employeeId, fullName, email, phone,
                department, role, designation,
                salary, leaveBalance, employmentStatus, managerId
        );

        employeeRepository.save(employee);
        return employee;
    }

    /*
     * Retrieves an employee by ID.
     *
     * @param employeeId - employee ID to search for
     * @return Employee object if found
     */
    public Employee getEmployeeById(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId);

        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }

        return employee;
    }

    /*
     * Retrieves all employees in the system.
     *
     * @return list of all employees
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeRepository.findAll());
    }

    /*
     * Updates an employee's email address.
     */
    public void updateEmployeeEmail(String employeeId, String newEmail) {
        Employee employee = getEmployeeById(employeeId);
        employee.setEmail(newEmail);
        employeeRepository.update(employee);
    }

    /*
     * Updates an employee's phone number.
     */
    public void updateEmployeePhone(String employeeId, String newPhone) {
        Employee employee = getEmployeeById(employeeId);
        employee.setPhone(newPhone);
        employeeRepository.update(employee);
    }

    /*
     * Updates an employee's department.
     */
    public void updateEmployeeDepartment(String employeeId, Department newDepartment) {
        Employee employee = getEmployeeById(employeeId);
        employee.setDepartment(newDepartment);
        employeeRepository.update(employee);
    }

    /*
     * Updates an employee's designation.
     */
    public void updateEmployeeDesignation(String employeeId, String newDesignation) {
        Employee employee = getEmployeeById(employeeId);
        employee.setDesignation(newDesignation);
        employeeRepository.update(employee);
    }

    /*
     * Updates an employee's salary.
     */
    public void updateEmployeeSalary(String employeeId, double newSalary) {
        Employee employee = getEmployeeById(employeeId);
        employee.setSalary(newSalary);
        employeeRepository.update(employee);
    }

    /*
     * Updates an employee's current employment status.
     */
    public void updateEmployeeStatus(String employeeId, EmploymentStatus newStatus) {
        Employee employee = getEmployeeById(employeeId);
        employee.setEmploymentStatus(newStatus);
        employeeRepository.update(employee);
    }

    /*
     * Updates an employee's leave balance.
     */
    public void updateEmployeeLeaveBalance(String employeeId, int newLeaveBalance) {
        Employee employee = getEmployeeById(employeeId);
        employee.setLeaveBalance(newLeaveBalance);
        employeeRepository.update(employee);
    }

    /*
     * Deletes an employee from the system.
     *
     * @param employeeId - employee ID to delete
     */
    public void deleteEmployee(String employeeId) {
        // Check that employee exists before attempting deletion
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Employee not found.");
        }

        employeeRepository.deleteById(employeeId);
    }

    /*
     * Retrieves all employees belonging to a specific department.
     *
     * @param departmentId - ID of the department
     * @return list of employees in that department
     */
    public List<Employee> getEmployeesByDepartment(String departmentId) {
        List<Employee> filteredEmployees = new ArrayList<>();

        // Filter employees manually by matching department ID
        for (Employee employee : employeeRepository.findAll()) {
            if (employee.getDepartment().getDepartmentId().equals(departmentId)) {
                filteredEmployees.add(employee);
            }
        }

        return filteredEmployees;
    }
}