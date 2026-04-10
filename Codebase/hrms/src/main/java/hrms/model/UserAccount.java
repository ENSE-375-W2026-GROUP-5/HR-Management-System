package hrms.model;

/*
 * Represents a user account used for authentication in the system.
 */
public class UserAccount {

    private String username;
    private String password;
    private String linkedEmployeeId;
    private Role role;

    /*
     * Constructor to initialize a UserAccount.
     */
    public UserAccount(String username, String password, String linkedEmployeeId, Role role) {
        this.username = username;
        this.password = password;
        this.linkedEmployeeId = linkedEmployeeId;
        this.role = role;
    }


    public String getUsername() { return username; }

    /*
     * Returns the password.
     * Here the password is not hidden of hashed unlike real systems where it is hidden
     */
    public String getPassword() { return password; }

    public String getLinkedEmployeeId() { return linkedEmployeeId; }

    public Role getRole() { return role; }
}