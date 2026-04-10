package hrms.model;

import java.time.LocalDate;

/*
 * Represents a leave request submitted by an employee.
 * Contains details such as leave duration, type, status, and approval info.
 */
public class LeaveRequest {

    private String requestId;
    private String employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfDays;
    private String reason;
    private LeaveStatus status;
    private String approverId;

    /*
     * Constructor to initialize a LeaveRequest object.
     */
    public LeaveRequest(String requestId, String employeeId, LeaveType leaveType,
                        LocalDate startDate, LocalDate endDate, int numberOfDays,
                        String reason, LeaveStatus status, String approverId) {

        this.requestId = requestId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.status = status;
        this.approverId = approverId;
    }


    public String getRequestId() { return requestId; }
    public String getEmployeeId() { return employeeId; }
    public LeaveType getLeaveType() { return leaveType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getNumberOfDays() { return numberOfDays; }
    public String getReason() { return reason; }
    public LeaveStatus getStatus() { return status; }
    public String getApproverId() { return approverId; }


    /*
     * Updates the status of the leave request.
     */
    public void setStatus(LeaveStatus status) { 
        this.status = status; 
    }

    /*
     * Sets the approver ID
     */
    public void setApproverId(String approverId) { 
        this.approverId = approverId; 
    }
}