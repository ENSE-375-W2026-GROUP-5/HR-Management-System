package hrms.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hrms.db.DatabaseConnection;
import hrms.model.Role;
import hrms.model.UserAccount;

/*
 * Repository class responsible for database operations
 * related to UserAccount entities.
 */
public class UserRepository {

    public void save(UserAccount userAccount) {
        String sql = "INSERT INTO users (username, password, linked_employee_id, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, userAccount.getUsername());
            ps.setString(2, userAccount.getPassword());
            ps.setString(3, userAccount.getLinkedEmployeeId());
            ps.setString(4, userAccount.getRole().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    /*
     * Finds a user by their username.
     *
     * @param username - username to search for
     * @return UserAccount if found, otherwise null
     */
    public UserAccount findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UserAccount(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("linked_employee_id"),
                        Role.valueOf(rs.getString("role"))
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user: " + e.getMessage(), e);
        }
    }

    /*
     * Checks if a user exists in the database.
     *
     * @param username - username to check
     * @return true if exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check user existence: " + e.getMessage(), e);
        }
    }

    /*
     * Deletes a user account from the database.
     */
    public void deleteByUsername(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    /*
     * Retrieves all user accounts from the database.
     *
     * @return collection of UserAccount objects
     */
    public Collection<UserAccount> findAll() {
        String sql = "SELECT * FROM users";
        List<UserAccount> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new UserAccount(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("linked_employee_id"),
                        Role.valueOf(rs.getString("role"))
                ));
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch users: " + e.getMessage(), e);
        }
    }
}