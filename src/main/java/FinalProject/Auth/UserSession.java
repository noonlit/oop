package FinalProject.Auth;

import FinalProject.Domain.DTO.UserDTO;

/**
 * Mocks a user session with a singleton.
 */
public final class UserSession {
    private static UserSession instance;
    private static UserDTO user;

    private UserSession(UserDTO userDTO) {
        user = userDTO;
    }

    /**
     * Starts a session, i.e. registers the given user.
     * To be called on login.
     */
    public static void startSession(UserDTO user) {
        if (instance == null) {
            instance = new UserSession(user);
        }
    }

    /**
     * @return The current user.
     */
    public static UserDTO getUser() {
        return user;
    }

    /**
     * Destroys a session, i.e. resets the current user to null.
     * To be called on logout.
     */
    public static void destroySession() {
        instance = null;
    }
}