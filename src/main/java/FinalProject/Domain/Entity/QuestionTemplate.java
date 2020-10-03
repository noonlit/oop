package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

public class QuestionTemplate extends AbstractEntity {
    @StorageField(field = "text")
    private String text;

    @StorageField(field = "expected_answer")
    private String expectedAnswer;

    @StorageField(field = "hint")
    private String hint;

    @StorageField(field = "quiz_id")
    private Integer quizId;

    public String getText() {
        return text;
    }

    public QuestionTemplate setText(String text) {
        this.text = text;
        return this;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public QuestionTemplate setExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
        return this;
    }

    public String getHint() {
        return hint;
    }

    public QuestionTemplate setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public QuestionTemplate setQuizId(Integer quizId) {
        this.quizId = quizId;
        return this;
    }

    @Override
    public String toString() {
        return text;
    }
}
