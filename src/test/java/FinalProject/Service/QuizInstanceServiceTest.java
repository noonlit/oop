package FinalProject.Service;

import FinalProject.Domain.DTO.QuizInstanceDTO;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Domain.Entity.QuestionInstance;
import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Domain.Entity.User;
import FinalProject.Domain.Validator.QuizInstanceValidator;
import FinalProject.Repository.Db.QuestionInstanceRepository;
import FinalProject.Repository.Db.QuizInstanceRepository;
import FinalProject.Repository.Db.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class QuizInstanceServiceTest {
    @InjectMocks
    QuizInstanceService service;

    @Mock
    QuizInstanceValidator validator;

    @Mock
    QuizInstanceRepository quizRepository;

    @Mock
    QuestionInstanceRepository questionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RepositoryManager repositoryManager;

    @Mock
    QuizInstance quiz;

    @Mock
    User user;

    @Mock
    UserDTO userDTO;

    @Mock
    QuestionInstance question;

    @Mock
    QuizInstanceDTO quizDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // stub repository manager methods to return mocks of actual repositories
        when(repositoryManager.getRepository(QuizInstanceRepository.class)).thenReturn(quizRepository);
        when(repositoryManager.getRepository(QuestionInstanceRepository.class)).thenReturn(questionRepository);
        when(repositoryManager.getRepository(UserRepository.class)).thenReturn(userRepository);
    }

    @Test
    void getAssociatedQuestionsShouldReturnTwoQuestionTemplates() {
        HashMap<Integer, QuestionInstance> map = new HashMap<>();
        map.put(1, question);
        map.put(2, question);
        when(questionRepository.getAssociatedQuestions(any(QuizInstance.class))).thenReturn(map);

        assertEquals(map.values(), service.getAssociatedQuestions(quiz));
    }

    @Test
    void saveQuizWithQuestionsShouldSaveAll() {
        when(quizRepository.save(any(QuizInstance.class), anyList())).thenReturn(true);

        List<QuestionInstance> list = new ArrayList<>();
        list.add(question);
        list.add(question);
        when(quizDTO.getQuiz()).thenReturn(quiz);
        when(quizDTO.getQuestions()).thenReturn(list);

        assertTrue(service.saveSolvedQuiz(quizDTO));
        verify(quizRepository, times(1)).save(quiz, list);
        verify(validator, times(1)).validate(quiz);
    }


    @Test
    void saveWithDTOShouldSaveQuiz() {
        when(quizRepository.save(any(QuizInstance.class))).thenReturn(true);
        when(quizDTO.getQuiz()).thenReturn(quiz);

        assertTrue(service.save(quizDTO));

        verify(quizRepository, times(1)).save(quiz);
        verify(validator, times(1)).validate(quiz);
    }

    @Test
    void getQuizzesDataForAdminListingShouldReturnTwoQuizDTOs() {
        HashMap<Integer, QuizInstance> map = new HashMap<>();
        map.put(1, quiz);
        map.put(2, quiz);
        when(quizRepository.getAll(QuizInstance.class)).thenReturn(map);
        when(quiz.getUserId()).thenReturn(1);
        when(userRepository.findOne(User.class, quiz.getUserId())).thenReturn(user);

        assertEquals(map.values().size(), service.getQuizzesDataForAdminListing().size());
    }


    @Test
    void getQuizzesDataForCandidateShouldReturnTwoQuizDTOs() {
        HashMap<Integer, QuizInstance> map = new HashMap<>();
        map.put(1, quiz);
        map.put(2, quiz);
        when(quizRepository.findAll(user)).thenReturn(map);
        when(userDTO.getUser()).thenReturn(user);

        assertEquals(map.values().size(), service.getQuizzesDataForUserScoresListing(userDTO).size());
    }
}