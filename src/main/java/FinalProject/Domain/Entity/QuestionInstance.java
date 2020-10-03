package FinalProject.Domain.Entity;

import FinalProject.Annotation.StorageField;

public class QuestionInstance extends AbstractEntity {
    @StorageField(field = "text")
    private String text;

    @StorageField(field = "answer")
    private String answer;

    @StorageField(field = "expected_answer")
    private String expectedAnswer;

    @StorageField(field = "question_template_id")
    private Integer questionTemplateId;

    @StorageField(field = "quiz_instance_id")
    private Integer quizInstanceId;

    public String getText() {
        return text;
    }

    public QuestionInstance setText(String text) {
        this.text = text;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public QuestionInstance setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public Integer getQuestionTemplateId() {
        return questionTemplateId;
    }

    public QuestionInstance setQuestionTemplateId(Integer questionTemplateId) {
        this.questionTemplateId = questionTemplateId;
        return this;
    }

    public Integer getQuizInstanceId() {
        return quizInstanceId;
    }

    public QuestionInstance setQuizInstanceId(Integer quizInstanceId) {
        this.quizInstanceId = quizInstanceId;
        return this;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public QuestionInstance setExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
        return this;
    }
}
