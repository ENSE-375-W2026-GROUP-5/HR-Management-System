package hrms.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hrms.db.DatabaseConnection;
import hrms.model.Department;
import hrms.model.Employee;
import hrms.model.EmploymentStatus;
import hrms.model.Role;

/*
 * Repository class responsible for all database operations
 * related to Employee objects.
 */
public class EmployeeRepository {

    public void save(Employee employee) {
        String sql = """
                INSERT INTO employees (
                    employee_id, full_name, email, phone, department_id, role,
                    designation, salary, leave_balance, employment_status, manager_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set values for the INSERT query
            ps.setString(1, employee.getEmployeeId());
            ps.setString(2, employee.getFullName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getPhone());
            ps.setString(5, employee.getDepartment().getDepartmentId());
            ps.setString(6, employee.getRole().name());
            ps.setString(7, employee.getDesignation());
            ps.setDouble(8, employee.getSalary());
            ps.setInt(9, employee.getLeaveBalance());
            ps.setString(10, employee.getEmploymentStatus().name());
            ps.setString(11, employee.getManagerId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save employee: " + e.getMessage(), e);
        }
    }

    /*
     * Finds an employee by their unique employee ID.
     * Also joins the departments table to retrieve department details.
     *
     * @param employeeId - ID of the employee to search for
     * @return Employee object if found, otherwise null
     */
    public Employee findById(String employeeId) {
        String sql = """
                SELECT e.*, d.department_id AS d_id, d.name AS d_name, d.description AS d_description
                FROM employees e
                JOIN departments d ON e.department_id = d.department_id
                WHERE e.employee_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set employee ID in query
            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Department department = new Department(
                        rs.getString("d_id"),
                        rs.getString("d_name"),
                        rs.getString("d_description")
                );

                return new Employee(
                        rs.getString("employee_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        department,
                        Role.valueOf(rs.getString("role")),
                        rs.getString("designation"),
                        rs.getDouble("salary"),
                        rs.getInt("leave_balance"),
                        EmploymentStatus.valueOf(rs.getString("employment_status")),
                        rs.getString("manager_id")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employee: " + e.getMessage(), e);
        }
    }

    /*
     * Checks whether an employee exists in the database.
     *
     * @param employeeId - ID of the employee
     * @return true if the employee exists, false otherwise
     */
    public boolean existsById(String employeeId) {
        String sql = "SELECT 1 FROM employees WHERE employee_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check employee existence: " + e.getMessage(), e);
        }
    }

    /*
     * Deletes an employee record from the database using employee ID.
     */
    public void deleteById(String employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, employeeId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete employee: " + e.getMessage(), e);
        }
    }

    /*
     * Retrieves all employees from the database.
     * Also joins the departments table to include department information.
     *
     * @return collection of Employee objects
     */
    public Collection<Employee> findAll() {
        String sql = """
                SELECT e.*, d.department_id AS d_id, d.name AS d_name, d.description AS d_description
                FROM employees e
                JOIN departments d ON e.department_id = d.department_id
                """;

        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Department department = new Department(
                        rs.getString("d_id"),
                        rs.getString("d_name"),
                        rs.getString("d_description")
                );

                employees.add(new Employee(
                        rs.getString("employee_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        department,
                        Role.valueOf(rs.getString("role")),
                        rs.getString("designation"),
                        rs.getDouble("salary"),
                        rs.getInt("leave_balance"),
                        EmploymentStatus.valueOf(rs.getString("employment_status")),
                        rs.getString("manager_id")
                ));
            }

            return employees;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employees: " + e.getMessage(), e);
        }
    }

    /*
     * Updates an existing employee's editable fields in the database.
     *
     * @param employee - Employee object containing updated information
     */
    public void update(Employee employee) {
        String sql = """
                UPDATE employees
                SET email = ?, phone = ?, department_id = ?, designation = ?, salary = ?,
                    leave_balance = ?, employment_status = ?, manager_id = ?
                WHERE employee_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, employee.getEmail());
            ps.setString(2, employee.getPhone());
            ps.setString(3, employee.getDepartment().getDepartmentId());
            ps.setString(4, employee.getDesignation());
            ps.setDouble(5, employee.getSalary());
            ps.setInt(6, employee.getLeaveBalance());
            ps.setString(7, employee.getEmploymentStatus().name());
            ps.setString(8, employee.getManagerId());
            ps.setString(9, employee.getEmployeeId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee: " + e.getMessage(), e);
        }
    }
}