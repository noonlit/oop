package FinalProject.Domain.DTO;

import FinalProject.Domain.Entity.Category;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a quiz template, a category, and a list of question templates.
 */
public class QuizTemplateDTO {
    private QuizTemplate quiz;
    private Category category;
    private List<QuestionTemplate> questions = new ArrayList<>();

    public QuizTemplate getQuiz() {
        return quiz;
    }

    public QuizTemplateDTO setQuiz(QuizTemplate storageQuiz) {
        this.quiz = storageQuiz;
        return this;
    }

    public Integer getId() {
        return quiz.getId();
    }

    public Category getCategory() {
        return category;
    }

    public QuizTemplateDTO setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getName() {
        return quiz.getName();
    }

    public String getDescription() {
        return quiz.getDescription();
    }

    public void addQuestion(QuestionTemplate question) {
        questions.add(question);
    }

    public List<QuestionTemplate> getQuestions() {
        return questions;
    }
}
