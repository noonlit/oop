package FinalProject.Domain.DTO;

import FinalProject.Domain.Entity.User;
import FinalProject.Domain.Entity.UserRole;

/**
 * Wraps a user and a role.
 */
public class UserDTO {
    private User user;
    private UserRole role;

    public Integer getId() {
        return user.getId();
    }

    public boolean isAdmin() {
        return role != null && role.isAdmin();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public User getUser() {
        return user;
    }

    public UserDTO setUser(User user) {
        this.user = user;
        return this;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
