package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

public class UserRole extends AbstractEntity {
    @StorageField(field = "label")
    private String label;

    @StorageField(field = "is_admin")
    private boolean isAdmin;

    public String getLabel() {
        return label;
    }

    public UserRole setLabel(String label) {
        this.label = label;
        return this;
    }

    public UserRole setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return label;
    }
}
