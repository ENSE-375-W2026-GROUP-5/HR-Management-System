package hrms.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import hrms.model.Employee;
import hrms.model.LeaveRequest;
import hrms.model.LeaveStatus;
import hrms.model.LeaveType;
import hrms.model.Role;
import hrms.repository.EmployeeRepository;
import hrms.repository.LeaveRepository;

/*
 * Service layer for handling leave-related logic.
 * Manages leave applications, approvals, rejections, cancellations,
 * and retrieval of leave requests.
 */
public class LeaveService {

    private final LeaveRepository leaveRepository;

    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    /*
     * Applies for a new leave request.
     *
     * @param requestId - unique leave request ID
     * @param employeeId - ID of the employee requesting leave
     * @param leaveType - type of leave
     * @param startDate - start date of leave
     * @param endDate - end date of leave
     * @param numberOfDays - number of leave days requested
     * @param reason - reason for leave
     * @return created LeaveRequest object
     */
    public LeaveRequest applyLeave(String requestId, String employeeId, LeaveType leaveType,
                                   LocalDate startDate, LocalDate endDate, int numberOfDays,
                                   String reason) {

        if (leaveRepository.existsById(requestId)) {
            throw new IllegalArgumentException("Leave request ID already exists.");
        }

        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }

        if (numberOfDays > employee.getLeaveBalance()) {
            throw new IllegalArgumentException("Insufficient leave balance.");
        }

        LeaveRequest leaveRequest = new LeaveRequest(
                requestId,
                employeeId,
                leaveType,
                startDate,
                endDate,
                numberOfDays,
                reason,
                LeaveStatus.PENDING,
                null
        );

        leaveRepository.save(leaveRequest);
        return leaveRequest;
    }

    /*
     * Retrieves a leave request by its ID.
     *
     * @param requestId - leave request ID
     * @return LeaveRequest if found
     */
    public LeaveRequest getLeaveRequestById(String requestId) {
        LeaveRequest leaveRequest = leaveRepository.findById(requestId);

        if (leaveRequest == null) {
            throw new IllegalArgumentException("Leave request not found.");
        }

        return leaveRequest;
    }

    /*
     * Retrieves all leave requests in the system.
     *
     * @return list of all leave requests
     */
    public List<LeaveRequest> getAllLeaveRequests() {
        return new ArrayList<>(leaveRepository.findAll());
    }

    /*
     * Approves a pending leave request.
     * Only authorized roles can approve leave.
     * Also deducts leave balance from the employee.
     *
     * @param requestId - leave request ID
     * @param approverEmployeeId - ID of the employee approving the request
     */
    public void approveLeave(String requestId, String approverEmployeeId) {
        LeaveRequest leaveRequest = getLeaveRequestById(requestId);
        Employee approver = employeeRepository.findById(approverEmployeeId);

        if (approver == null) {
            throw new IllegalArgumentException("Approver not found.");
        }

        if (approver.getRole() != Role.ADMIN &&
            approver.getRole() != Role.HR_MANAGER &&
            approver.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("Unauthorized to approve leave.");
        }

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only pending leave requests can be approved.");
        }

        Employee employee = employeeRepository.findById(leaveRequest.getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }

        employee.setLeaveBalance(employee.getLeaveBalance() - leaveRequest.getNumberOfDays());

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setApproverId(approverEmployeeId);

        employeeRepository.update(employee);
        leaveRepository.update(leaveRequest);
    }

    /*
     * Rejects a pending leave request.
     * Only authorized roles can reject leave.
     *
     * @param requestId - leave request ID
     * @param approverEmployeeId - ID of the employee rejecting the request
     */
    public void rejectLeave(String requestId, String approverEmployeeId) {
        LeaveRequest leaveRequest = getLeaveRequestById(requestId);
        Employee approver = employeeRepository.findById(approverEmployeeId);

        if (approver == null) {
            throw new IllegalArgumentException("Approver not found.");
        }

        if (approver.getRole() != Role.ADMIN &&
            approver.getRole() != Role.HR_MANAGER &&
            approver.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("Unauthorized to reject leave.");
        }

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only pending leave requests can be rejected.");
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequest.setApproverId(approverEmployeeId);
        leaveRepository.update(leaveRequest);
    }

    /*
     * Cancels a pending leave request.
     * Employees are only allowed to cancel their own leave requests.
     *
     * @param requestId - leave request ID
     * @param employeeId - ID of the employee cancelling the request
     */
    public void cancelLeave(String requestId, String employeeId) {
        LeaveRequest leaveRequest = getLeaveRequestById(requestId);

        if (!leaveRequest.getEmployeeId().equals(employeeId)) {
            throw new IllegalArgumentException("Employees can only cancel their own leave requests.");
        }

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only pending leave requests can be cancelled.");
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);
        leaveRepository.update(leaveRequest);
    }

    /*
     * Retrieves all leave requests made by a specific employee.
     *
     * @param employeeId - employee ID
     * @return list of leave requests for that employee
     */
    public List<LeaveRequest> getLeaveRequestsByEmployee(String employeeId) {
        List<LeaveRequest> employeeLeaves = new ArrayList<>();

        for (LeaveRequest leaveRequest : leaveRepository.findAll()) {
            if (leaveRequest.getEmployeeId().equals(employeeId)) {
                employeeLeaves.add(leaveRequest);
            }
        }

        return employeeLeaves;
    }
}