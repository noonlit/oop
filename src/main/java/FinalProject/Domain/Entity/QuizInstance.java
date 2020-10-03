package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

public class QuizInstance extends AbstractEntity {
    @StorageField(field = "name")
    private String name;

    @StorageField(field = "category_label")
    private String categoryLabel;

    @StorageField(field = "description")
    private String description;

    @StorageField(field = "score")
    private Integer score;

    @StorageField(field = "user_id")
    private Integer userId;

    @StorageField(field = "quiz_template_id")
    private Integer quizTemplateId;

    public String getName() {
        return name;
    }

    public QuizInstance setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public QuizInstance setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public QuizInstance setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getScore() {
        return score;
    }

    public QuizInstance setScore(Integer score) {
        this.score = score;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public QuizInstance setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getQuizTemplateId() {
        return quizTemplateId;
    }

    public QuizInstance setQuizTemplateId(Integer quizTemplateId) {
        this.quizTemplateId = quizTemplateId;
        return this;
    }
}
