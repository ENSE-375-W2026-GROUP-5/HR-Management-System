package hrms.model;

/*
 * Represents a department within the company
 * Contains basic information such as ID, name, and description.
 */
public class Department {

    private String departmentId;

    // Name of the department
    private String name;

    // Optional description of the department
    private String description;

    /*
     * Constructor to initialize a Department object.
     */
    public Department(String departmentId, String name, String description) {

        // Validate department ID
        if (departmentId == null || departmentId.isBlank()) {
            throw new IllegalArgumentException("Department ID cannot be empty.");
        }

        // Validate department name
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Department name cannot be empty.");
        }

        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
    }

    /*
     * Returns the department ID.
     */
    public String getDepartmentId() {
        return departmentId;
    }

    /*
     * Returns the department name.
     */
    public String getName() {
        return name;
    }

    /*
     * Returns the department description.
     */
    public String getDescription() {
        return description;
    }

    /*
     * Updates the department name.
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Department name cannot be empty.");
        }
        this.name = name;
    }

    /*
     * Updates the department description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}