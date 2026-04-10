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

/*
 * Repository class responsible for performing database operations
 * related to the Department entity.
 */
public class DepartmentRepository {

    public void save(Department department) {
        String sql = "INSERT INTO departments (department_id, name, description) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set values for the SQL query parameters
            ps.setString(1, department.getDepartmentId());
            ps.setString(2, department.getName());
            ps.setString(3, department.getDescription());

            // Execute INSERT query
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save department: " + e.getMessage(), e);
        }
    }

    /*
     * Finds a department by its unique ID.
     *
     * @param departmentId - ID of the department to search for
     * @return Department object if found, otherwise null
     */
    public Department findById(String departmentId) {
        String sql = "SELECT * FROM departments WHERE department_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, departmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Department(
                        rs.getString("department_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch department: " + e.getMessage(), e);
        }
    }

    /*
     * Checks whether a department exists in the database.
     *
     * @param departmentId - ID of the department
     * @return true if found, false otherwise
     */
    public boolean existsById(String departmentId) {
        String sql = "SELECT 1 FROM departments WHERE department_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, departmentId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check department existence: " + e.getMessage(), e);
        }
    }

    /*
     * Deletes a department from the database using its ID.
     */
    public void deleteById(String departmentId) {
        String sql = "DELETE FROM departments WHERE department_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, departmentId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete department: " + e.getMessage(), e);
        }
    }

    /*
     * Retrieves all departments from the database.
     *
     * @return collection of all Department objects
     */
    public Collection<Department> findAll() {
        String sql = "SELECT * FROM departments";
        List<Department> departments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                departments.add(new Department(
                        rs.getString("department_id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }

            return departments;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch departments: " + e.getMessage(), e);
        }
    }
}