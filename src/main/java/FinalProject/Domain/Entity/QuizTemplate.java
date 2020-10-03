package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

public class QuizTemplate extends AbstractEntity {
    @StorageField(field = "name")
    private String name;

    @StorageField(field = "category_id")
    private Integer categoryId;

    @StorageField(field = "description")
    private String description;

    public String getName() {
        return name;
    }

    public QuizTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public QuizTemplate setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public QuizTemplate setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
