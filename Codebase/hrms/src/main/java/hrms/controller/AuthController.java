package hrms.controller;

import hrms.model.Role;
import hrms.model.UserAccount;
import hrms.service.AuthService;

/*
 * Controller layer for handling authentication-related requests.
 */
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
     * Registers a new user in the system.
     * Delegates the actual logic to AuthService.
     *
     * @param username - unique username for login
     * @param password - user's password
     * @param linkedEmployeeId - employee ID associated with this account
     * @param role - role assigned to the user (e.g., ADMIN, EMPLOYEE)
     * @return the created UserAccount object
     */
    public UserAccount registerUser(String username, String password, String linkedEmployeeId, Role role) {
        return authService.registerUser(username, password, linkedEmployeeId, role);
    }

    /*
     * Authenticates a user using username and password.
     *
     * @param username - user's username
     * @param password - user's password
     * @return UserAccount if login is successful, otherwise it returns and exception 
     */
    public UserAccount login(String username, String password) {
        return authService.login(username, password);
    }

    /*
     * Checks if a given user has a specific role.
     *
     * @param username - user's username
     * @param role - role to check against
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String username, Role role) {
        return authService.hasRole(username, role);
    }
}