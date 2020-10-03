package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

abstract public class AbstractEntity {
    @StorageField(field = "id")
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public AbstractEntity setId(Integer id) {
        this.id = id;
        return this;
    }
}
