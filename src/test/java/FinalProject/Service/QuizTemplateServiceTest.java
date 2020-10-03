package FinalProject.Service;

import FinalProject.Domain.DTO.QuizTemplateDTO;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Domain.Entity.Category;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Domain.Entity.User;
import FinalProject.Domain.Validator.QuestionValidator;
import FinalProject.Domain.Validator.QuizValidator;
import FinalProject.Repository.Db.CategoryRepository;
import FinalProject.Repository.Db.QuestionTemplateRepository;
import FinalProject.Repository.Db.QuizTemplateRepository;
import FinalProject.Repository.RepositoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuizTemplateServiceTest {
    @InjectMocks
    QuizTemplateService service;

    @Mock
    QuizTemplateRepository quizRepository;

    @Mock
    QuestionTemplateRepository questionRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    QuizValidator quizValidator;

    @Mock
    QuestionValidator questionValidator;

    @Mock
    RepositoryManager repositoryManager;

    @Mock
    QuizTemplate quiz;

    @Mock
    QuestionTemplate question;

    @Mock
    Category category;

    @Mock
    User user;

    @Mock
    UserDTO userDTO;

    @Mock
    QuizTemplateDTO quizDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // stub repository manager methods to return mocks of actual repositories
        when(repositoryManager.getRepository(QuizTemplateRepository.class)).thenReturn(quizRepository);
        when(repositoryManager.getRepository(QuestionTemplateRepository.class)).thenReturn(questionRepository);
        when(repositoryManager.getRepository(CategoryRepository.class)).thenReturn(categoryRepository);
    }

    @Test
    void getAssociatedQuestionsShouldReturnTwoQuestionTemplates() {
        HashMap<Integer, QuestionTemplate> map = new HashMap<>();
        map.put(1, question);
        map.put(2, question);
        when(questionRepository.getAssociatedQuestions(any(QuizTemplate.class))).thenReturn(map);

        assertEquals(map.values(), service.getAssociatedQuestions(quiz));
    }

    @Test
    void getEligibleQuestionsShouldReturnTwoQuestionTemplates() {
        HashMap<Integer, QuestionTemplate> map = new HashMap<>();
        map.put(1, question);
        map.put(2, question);
        when(questionRepository.getQuestionsAvailableForQuizAssociation()).thenReturn(map);

        assertEquals(map.values(), service.getEligibleQuestions());
    }

    @Test
    void getEligibleQuestionsForQuizShouldReturnTwoQuestionTemplates() {
        HashMap<Integer, QuestionTemplate> map = new HashMap<>();
        map.put(1, question);
        map.put(2, question);
        when(questionRepository.getQuestionsAvailableForQuizAssociation(quiz)).thenReturn(map);

        assertEquals(map.values(), service.getEligibleQuestions(quiz));
    }

    @Test
    void getQuizCategoriesShouldReturnTwoCategories() {
        HashMap<Integer, Category> map = new HashMap<>();
        map.put(1, category);
        map.put(2, category);
        when(categoryRepository.getAll(Category.class)).thenReturn(map);

        assertEquals(map.values(), service.getQuizCategories());
    }

    @Test
    void getCategoryForQuizShouldReturnOne() {
        when(categoryRepository.getCategoryForQuiz(any(QuizTemplate.class))).thenReturn(category);
        assertEquals(category, service.getCategoryForQuiz(quiz));
    }

    @Test
    void deleteShouldDeleteQuiz() {
        when(quizRepository.delete(any(QuizTemplate.class))).thenReturn(true);
        when(quizDTO.getQuiz()).thenReturn(quiz);
        assertTrue(service.delete(quizDTO));
        verify(quizRepository, times(1)).delete(quiz);
    }

    @Test
    void saveShouldSaveQuiz() {
        when(quizRepository.save(any(QuizTemplate.class))).thenReturn(true);

        assertTrue(service.save(quiz));

        verify(quizValidator, times(1)).validate(quiz);
        verify(quizRepository, times(1)).save(quiz);
    }

    @Test
    void saveWithDTOShouldSaveQuiz() {
        when(quizRepository.save(any(QuizTemplate.class))).thenReturn(true);
        when(quizDTO.getQuiz()).thenReturn(quiz);

        assertTrue(service.save(quizDTO));

        verify(quizValidator, times(1)).validate(quiz);
        verify(quizRepository, times(1)).save(quiz);
    }

    @Test
    void createQuizWithQuestionsShouldSaveAll() {
        when(quizRepository.create(any(QuizTemplate.class), anyList())).thenReturn(true);

        List<QuestionTemplate> list = new ArrayList<>();
        list.add(question);
        list.add(question);
        when(quizDTO.getQuiz()).thenReturn(quiz);
        when(quizDTO.getQuestions()).thenReturn(list);

        assertTrue(service.createQuizWithQuestions(quizDTO));
        verify(quizValidator, times(1)).validate(quiz);
        verify(questionValidator, times(2)).validate(question);
        verify(quizRepository, times(1)).create(quiz, list);
    }

    @Test
    void updateQuizWithQuestionsShouldSaveAll() {
        when(quizRepository.update(any(QuizTemplate.class), anyList())).thenReturn(true);

        List<QuestionTemplate> list = new ArrayList<>();
        list.add(question);
        list.add(question);
        when(quizDTO.getQuiz()).thenReturn(quiz);
        when(quizDTO.getQuestions()).thenReturn(list);

        assertTrue(service.updateQuizWithQuestions(quizDTO));
        verify(quizValidator, times(1)).validate(quiz);
        verify(questionValidator, times(2)).validate(question);
        verify(quizRepository, times(1)).update(quiz, list);
    }

    @Test
    void getQuizzesDataForAdminListingShouldReturnTwoQuizDTOs() {
        HashMap<Integer, QuizTemplate> map = new HashMap<>();
        map.put(1, quiz);
        map.put(2, quiz);
        when(quizRepository.getAll(QuizTemplate.class)).thenReturn(map);

        assertEquals(map.values().size(), service.getQuizzesDataForAdminListing().size());
    }

    @Test
    void getQuizzesDataForCandidateListingShouldReturnTwoQuizDTOs() {
        HashMap<Integer, QuizTemplate> map = new HashMap<>();
        map.put(1, quiz);
        map.put(2, quiz);
        when(quizRepository.getAllQuizzesAvailableForUser(user)).thenReturn(map);
        when(userDTO.getUser()).thenReturn(user);

        assertEquals(map.values().size(), service.getQuizzesDataForCandidateListing(userDTO).size());
    }
}