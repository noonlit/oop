package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

/**
 * Wraps a db row of user data.
 */
public class User extends AbstractEntity {
    @StorageField(field = "email")
    private String email;

    @StorageField(field = "password")
    private String password;

    @StorageField(field = "role_id")
    private Integer roleId;

    /**
     * Role ID getter.
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * Fluent role id setter.
     */
    public User setRoleId(int roleId) {
        this.roleId = roleId;
        return this;
    }

    /**
     * Email getter.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Fluent email setter.
     */
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Password getter.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Fluent password setter.
     */
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return email;
    }
}
