package FinalProject.Domain.DTO;

import FinalProject.Domain.Entity.QuestionInstance;
import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Domain.Entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a quiz instance, a list of question instances, and a user.
 */
public class QuizInstanceDTO {
    private QuizInstance quiz;
    private List<QuestionInstance> questionList = new ArrayList<>();
    private User user;

    public Integer getId() {
        return quiz.getId();
    }

    public User getUser() {
        return user;
    }

    public QuizInstanceDTO setUser(User user) {
        this.user = user;
        return this;
    }

    public QuizInstance getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizInstance quizInstance) {
        this.quiz = quizInstance;
    }

    public QuizInstanceDTO addQuestion(QuestionInstance questionInstance) {
        questionList.add(questionInstance);
        return this;
    }

    public List<QuestionInstance> getQuestions() {
        return questionList;
    }

    public String getName() {
        return quiz.getName();
    }

    public String getCategoryLabel() {
        return quiz.getCategoryLabel();
    }

    public String getDescription() {
        return quiz.getDescription();
    }

    public Integer getScore() {
        return quiz.getScore();
    }

    public Integer getUserId() {
        return quiz.getUserId();
    }

    public Integer getQuizTemplateId() {
        return quiz.getQuizTemplateId();
    }
}
