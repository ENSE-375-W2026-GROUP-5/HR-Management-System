package hrms.controller;

import java.time.LocalDate;
import java.util.List;

import hrms.model.LeaveRequest;
import hrms.model.LeaveType;
import hrms.service.LeaveService;

/*
 * Controller layer for handling leave management operations.
 */
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    /*
     * Applies for a new leave request.
     *
     * @param requestId - unique identifier for the leave request
     * @param employeeId - ID of the employee applying for leave
     * @param leaveType - type of leave (e.g., SICK, VACATION)
     * @param startDate - leave start date
     * @param endDate - leave end date
     * @param numberOfDays - total number of leave days
     * @param reason - reason for leave
     * @return the created LeaveRequest object
     */
    public LeaveRequest applyLeave(String requestId, String employeeId, LeaveType leaveType,
                                   LocalDate startDate, LocalDate endDate, int numberOfDays,
                                   String reason) {

        return leaveService.applyLeave(
                requestId, employeeId, leaveType,
                startDate, endDate, numberOfDays, reason
        );
    }

    /*
     * Retrieves a leave request by its ID.
     */
    public LeaveRequest getLeaveRequestById(String requestId) {
        return leaveService.getLeaveRequestById(requestId);
    }

    /*
     * Retrieves all leave requests in the system.
     */
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveService.getAllLeaveRequests();
    }

    /*
     * Approves a leave request.
     *
     * @param requestId - ID of the leave request
     * @param approverEmployeeId - ID of the employee approving the request
     */
    public void approveLeave(String requestId, String approverEmployeeId) {
        leaveService.approveLeave(requestId, approverEmployeeId);
    }

    /*
     * Rejects a leave request.
     *
     * @param requestId - ID of the leave request
     * @param approverEmployeeId - ID of the employee rejecting the request
     */
    public void rejectLeave(String requestId, String approverEmployeeId) {
        leaveService.rejectLeave(requestId, approverEmployeeId);
    }

    /*
     * Cancels a leave request
     *
     * @param requestId - ID of the leave request
     * @param employeeId - ID of the employee cancelling the leave
     */
    public void cancelLeave(String requestId, String employeeId) {
        leaveService.cancelLeave(requestId, employeeId);
    }

    /*
     * Retrieves all leave requests submitted by a specific employee.
     *
     * @param employeeId - ID of the employee
     * @return list of leave requests for that employee
     */
    public List<LeaveRequest> getLeaveRequestsByEmployee(String employeeId) {
        return leaveService.getLeaveRequestsByEmployee(employeeId);
    }
}