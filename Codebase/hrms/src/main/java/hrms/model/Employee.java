package hrms.model;

/*
 * Represents an employee
 * Contains personal, job, and employment-related details.
 */
public class Employee {

    private String employeeId;
    private String fullName;
    private String email;
    private String phone;
    private Department department;
    private Role role;
    private String designation;
    private double salary;
    private int leaveBalance;
    private EmploymentStatus employmentStatus;
    private String managerId;

    /*
     * Constructor to initialize an Employee object.
     */
    public Employee(String employeeId, String fullName, String email, String phone,
                    Department department, Role role, String designation,
                    double salary, int leaveBalance,
                    EmploymentStatus employmentStatus, String managerId) {

        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty.");
        }

        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }

        if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Designation cannot be empty.");
        }

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        if (leaveBalance < 0) {
            throw new IllegalArgumentException("Leave balance cannot be negative.");
        }

        if (employmentStatus == null) {
            throw new IllegalArgumentException("Employment status cannot be null.");
        }

        this.employeeId = employeeId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.role = role;
        this.designation = designation;
        this.salary = salary;
        this.leaveBalance = leaveBalance;
        this.employmentStatus = employmentStatus;
        this.managerId = managerId; // optional field
    }


    public String getEmployeeId() { return employeeId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Department getDepartment() { return department; }
    public Role getRole() { return role; }
    public String getDesignation() { return designation; }
    public double getSalary() { return salary; }
    public int getLeaveBalance() { return leaveBalance; }
    public EmploymentStatus getEmploymentStatus() { return employmentStatus; }
    public String getManagerId() { return managerId; }


    /*
     * Updates employee email.
     */
    public void setEmail(String email) { 
        this.email = email; 
    }

    /*
     * Updates employee phone number.
     */
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    /*
     * Updates employee department.
     */
    public void setDepartment(Department department) { 
        this.department = department; 
    }

    /*
     * Updates employee designation.
     */
    public void setDesignation(String designation) { 
        this.designation = designation; 
    }

    /*
     * Updates employee salary.
     */
    public void setSalary(double salary) { 
        this.salary = salary; 
    }

    /*
     * Updates employee leave balance.
     */
    public void setLeaveBalance(int leaveBalance) { 
        this.leaveBalance = leaveBalance; 
    }

    /*
     * Updates employment status.
     */
    public void setEmploymentStatus(EmploymentStatus employmentStatus) { 
        this.employmentStatus = employmentStatus; 
    }
}