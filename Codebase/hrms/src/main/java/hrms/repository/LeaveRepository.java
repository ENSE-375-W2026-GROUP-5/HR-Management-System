package hrms.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hrms.db.DatabaseConnection;
import hrms.model.LeaveRequest;
import hrms.model.LeaveStatus;
import hrms.model.LeaveType;

/*
 * Repository class responsible for database operations
 * related to LeaveRequest entities.
 */
public class LeaveRepository {

    public void save(LeaveRequest leaveRequest) {
        String sql = """
                INSERT INTO leave_requests (
                    request_id, employee_id, leave_type, start_date, end_date,
                    number_of_days, reason, status, approver_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set parameters for INSERT query
            ps.setString(1, leaveRequest.getRequestId());
            ps.setString(2, leaveRequest.getEmployeeId());
            ps.setString(3, leaveRequest.getLeaveType().name());

            // Dates are stored as strings (ISO format)
            ps.setString(4, leaveRequest.getStartDate().toString());
            ps.setString(5, leaveRequest.getEndDate().toString());

            ps.setInt(6, leaveRequest.getNumberOfDays());
            ps.setString(7, leaveRequest.getReason());
            ps.setString(8, leaveRequest.getStatus().name());
            ps.setString(9, leaveRequest.getApproverId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save leave request: " + e.getMessage(), e);
        }
    }

    /*
     * Retrieves a leave request by its ID.
     *
     * @param requestId - unique ID of the leave request
     * @return LeaveRequest object if found, otherwise null
     */
    public LeaveRequest findById(String requestId) {
        String sql = "SELECT * FROM leave_requests WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new LeaveRequest(
                        rs.getString("request_id"),
                        rs.getString("employee_id"),
                        LeaveType.valueOf(rs.getString("leave_type")),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date")),
                        rs.getInt("number_of_days"),
                        rs.getString("reason"),
                        LeaveStatus.valueOf(rs.getString("status")),
                        rs.getString("approver_id")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch leave request: " + e.getMessage(), e);
        }
    }

    /*
     * Checks if a leave request exists in the database.
     *
     * @param requestId - ID of the leave request
     * @return true if exists, false otherwise
     */
    public boolean existsById(String requestId) {
        String sql = "SELECT 1 FROM leave_requests WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, requestId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check leave request existence: " + e.getMessage(), e);
        }
    }

    /*
     * Deletes a leave request from the database.
     */
    public void deleteById(String requestId) {
        String sql = "DELETE FROM leave_requests WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, requestId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete leave request: " + e.getMessage(), e);
        }
    }

    /*
     * Retrieves all leave requests from the database.
     *
     * @return collection of LeaveRequest objects
     */
    public Collection<LeaveRequest> findAll() {
        String sql = "SELECT * FROM leave_requests";
        List<LeaveRequest> requests = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Convert each row into a LeaveRequest object
            while (rs.next()) {
                requests.add(new LeaveRequest(
                        rs.getString("request_id"),
                        rs.getString("employee_id"),
                        LeaveType.valueOf(rs.getString("leave_type")),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date")),
                        rs.getInt("number_of_days"),
                        rs.getString("reason"),
                        LeaveStatus.valueOf(rs.getString("status")),
                        rs.getString("approver_id")
                ));
            }

            return requests;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch leave requests: " + e.getMessage(), e);
        }
    }

    /*
     * Updates the status and approver of an existing leave request.
     *
     * @param leaveRequest - LeaveRequest object with updated values
     */
    public void update(LeaveRequest leaveRequest) {
        String sql = "UPDATE leave_requests SET status = ?, approver_id = ? WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set updated values
            ps.setString(1, leaveRequest.getStatus().name());
            ps.setString(2, leaveRequest.getApproverId());
            ps.setString(3, leaveRequest.getRequestId());

            // Execute UPDATE
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update leave request: " + e.getMessage(), e);
        }
    }
}