package FinalProject.Service;

import FinalProject.Domain.DTO.QuizTemplateDTO;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Domain.Entity.Category;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Domain.Validator.QuestionValidator;
import FinalProject.Domain.Validator.QuizValidator;
import FinalProject.Repository.Db.CategoryRepository;
import FinalProject.Repository.Db.QuestionTemplateRepository;
import FinalProject.Repository.Db.QuizTemplateRepository;
import FinalProject.Repository.RepositoryManager;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class QuizTemplateService {
    private final QuizValidator quizValidator;
    private final QuestionValidator questionValidator;
    private final RepositoryManager repositoryManager;

    @Inject
    public QuizTemplateService(RepositoryManager repositoryManager, QuizValidator quizValidator, QuestionValidator questionValidator) {
        this.repositoryManager = repositoryManager;
        this.quizValidator = quizValidator;
        this.questionValidator = questionValidator;
    }

    /**
     * Creates a new quiz based on a quiz template.
     */
    public boolean save(@NotNull QuizTemplateDTO quizDTO) {
        return save(quizDTO.getQuiz());
    }

    /**
     * Creates a new quiz.
     */
    public boolean save(QuizTemplate quiz) {
        quizValidator.validate(quiz);
        return getDefaultRepository().save(quiz);
    }

    /**
     * Returns a list of quiz data to be displayed in the admin listing.
     */
    public List<QuizTemplateDTO> getQuizzesDataForAdminListing() {
        HashMap<Integer, QuizTemplate> quizzes = getDefaultRepository().getAll(QuizTemplate.class);


        List<QuizTemplateDTO> quizTemplateList = new ArrayList<>();
        for (Map.Entry<Integer, QuizTemplate> entry : quizzes.entrySet()) {
            QuizTemplate quiz = entry.getValue();
            QuizTemplateDTO appQuiz = new QuizTemplateDTO();
            appQuiz.setQuiz(quiz);
            appQuiz.setCategory(getCategoryForQuiz(quiz));
            quizTemplateList.add(appQuiz);
        }

        return quizTemplateList;
    }

    /**
     * Retrieves the category associated with the given quiz.
     */
    public Category getCategoryForQuiz(QuizTemplate quiz) {
        CategoryRepository categoryRepository = (CategoryRepository) repositoryManager.getRepository(CategoryRepository.class);
        return categoryRepository.getCategoryForQuiz(quiz);
    }

    /**
     * Returns a list of quiz data to be displayed in the candidate listing.
     * The list excludes quizzes that the user has already taken and quizzes that have no questions.
     */
    public List<QuizTemplateDTO> getQuizzesDataForCandidateListing(@NotNull UserDTO currentUser) {
        Map<Integer, QuizTemplate> quizzes = getDefaultRepository().getAllQuizzesAvailableForUser(currentUser.getUser());
        CategoryRepository categoryRepository = (CategoryRepository) repositoryManager.getRepository(CategoryRepository.class);

        List<QuizTemplateDTO> quizTemplateList = new ArrayList<>();
        for (Map.Entry<Integer, QuizTemplate> entry : quizzes.entrySet()) {
            QuizTemplate quiz = entry.getValue();
            QuizTemplateDTO appQuiz = new QuizTemplateDTO();
            appQuiz.setQuiz(quiz);
            appQuiz.setCategory(categoryRepository.getCategoryForQuiz(quiz));
            quizTemplateList.add(appQuiz);
        }

        return quizTemplateList;
    }

    /**
     * Updates the given quiz and its questions transactionally.
     */
    public boolean updateQuizWithQuestions(@NotNull QuizTemplateDTO quizDTO) {
        QuizTemplate quiz = quizDTO.getQuiz();
        quizValidator.validate(quiz);

        List<QuestionTemplate> questions = quizDTO.getQuestions();
        for (QuestionTemplate question : questions) {
            questionValidator.validate(question);
        }

        return getDefaultRepository().update(quiz, questions);
    }

    /**
     * Creates the given quiz and associates its questions transactionally.
     */
    public boolean createQuizWithQuestions(@NotNull QuizTemplateDTO quizDTO) {
        QuizTemplate quiz = quizDTO.getQuiz();
        quizValidator.validate(quiz);

        List<QuestionTemplate> questions = quizDTO.getQuestions();
        for (QuestionTemplate question : questions) {
            questionValidator.validate(question);
        }

        return getDefaultRepository().create(quiz, questions);
    }

    /**
     * Deletes a quiz.
     */
    public boolean delete(@NotNull QuizTemplateDTO appQuiz) {
        return getDefaultRepository().delete(appQuiz.getQuiz());
    }

    /**
     * Returns eligible questions for a particular quiz id,
     * i.e. questions that are either associated to it or are not associated to any other quiz.
     */
    public Collection<QuestionTemplate> getEligibleQuestions(QuizTemplate quiz) {
        QuestionTemplateRepository questionTemplateRepository = (QuestionTemplateRepository) repositoryManager.getRepository(QuestionTemplateRepository.class);
        return questionTemplateRepository.getQuestionsAvailableForQuizAssociation(quiz).values();
    }

    /**
     * Returns all categories that can be assigned to quizzes.
     */
    public Collection<Category> getQuizCategories() {
        CategoryRepository categoryRepository = (CategoryRepository) repositoryManager.getRepository(CategoryRepository.class);
        return categoryRepository.getAll(Category.class).values();
    }

    /**
     * Retrieves questions associated with the given quiz.
     */
    public Collection<QuestionTemplate> getAssociatedQuestions(@NotNull QuizTemplate quiz) {
        QuestionTemplateRepository questionTemplateRepository = (QuestionTemplateRepository) repositoryManager.getRepository(QuestionTemplateRepository.class);
        return questionTemplateRepository.getAssociatedQuestions(quiz).values();
    }

    /**
     * Retrieves questions that can be associated to a quiz (i.e. are not associated to any quiz currently).
     */
    public Collection<QuestionTemplate> getEligibleQuestions() {
        QuestionTemplateRepository questionTemplateRepository = (QuestionTemplateRepository) repositoryManager.getRepository(QuestionTemplateRepository.class);
        return questionTemplateRepository.getQuestionsAvailableForQuizAssociation().values();
    }

    /**
     * Returns this service's default repository, which is the quiz template repository.
     */
    private QuizTemplateRepository getDefaultRepository() {
        return (QuizTemplateRepository) repositoryManager.getRepository(QuizTemplateRepository.class);
    }
}
