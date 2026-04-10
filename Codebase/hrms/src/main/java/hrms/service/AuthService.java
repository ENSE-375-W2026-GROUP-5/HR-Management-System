package hrms.service;

import hrms.model.Role;
import hrms.model.UserAccount;
import hrms.repository.UserRepository;

/*
 * Service layer for handling authentication logic.
 */
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Registers a new user in the system.
     *
     * @param username - unique username
     * @param password - user password
     * @param linkedEmployeeId - associated employee ID
     * @param role - assigned role
     * @return created UserAccount
     */
    public UserAccount registerUser(String username, String password, String linkedEmployeeId, Role role) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        UserAccount userAccount = new UserAccount(username, password, linkedEmployeeId, role);

        userRepository.save(userAccount);

        return userAccount;
    }

    /*
     * Authenticates a user using username and password.
     *
     * @param username - user's username
     * @param password - user's password
     * @return UserAccount if credentials are valid
     */
    public UserAccount login(String username, String password) {

        UserAccount userAccount = userRepository.findByUsername(username);

        if (userAccount == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (!userAccount.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return userAccount;
    }

    /*
     * Checks if a user has a specific role.
     *
     * @param username - user's username
     * @param role - role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String username, Role role) {

        UserAccount userAccount = userRepository.findByUsername(username);

        if (userAccount == null) {
            throw new IllegalArgumentException("User not found.");
        }

        return userAccount.getRole() == role;
    }
}