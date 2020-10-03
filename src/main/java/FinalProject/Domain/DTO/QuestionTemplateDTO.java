package FinalProject.Domain.DTO;

import FinalProject.Domain.Entity.QuestionTemplate;

/**
 * Wraps a question template.
 */
public class QuestionTemplateDTO {
    private QuestionTemplate question;

    public Integer getId() {
        return question.getId();
    }

    public QuestionTemplate getQuestion() {
        return question;
    }

    public QuestionTemplateDTO setQuestion(QuestionTemplate question) {
        this.question = question;
        return this;
    }

    public String getText() {
        return question.getText();
    }

    public String getExpectedAnswer() {
        return question.getExpectedAnswer();
    }

    public String getHint() {
        return question.getHint();
    }

    public Integer getQuizId() {
        return question.getQuizId();
    }
}
