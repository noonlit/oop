package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

public class Category extends AbstractEntity {
    @StorageField(field = "label")
    private String label;

    public String getLabel() {
        return label;
    }

    public Category setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public String toString() {
        return label;
    }
}
