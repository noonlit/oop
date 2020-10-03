package FinalProject.Service;

import FinalProject.Domain.DTO.QuestionTemplateDTO;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Validator.QuestionValidator;
import FinalProject.Repository.Db.QuestionTemplateRepository;
import FinalProject.Repository.RepositoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionTemplateServiceTest {
    @InjectMocks
    QuestionTemplateService service;

    @Mock
    QuestionTemplateRepository questionRepository;

    @Mock
    QuestionValidator questionValidator;

    @Mock
    RepositoryManager repositoryManager;

    @Mock
    QuestionTemplate question;

    @Mock
    QuestionTemplateDTO questionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // stub repository manager methods to return mocks of actual repositories
        when(repositoryManager.getRepository(QuestionTemplateRepository.class)).thenReturn(questionRepository);
    }

    @Test
    void saveShouldSaveQuestion() {
        when(questionRepository.save(any(QuestionTemplate.class))).thenReturn(true);
        assertTrue(service.save(question));
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void updateShouldSaveQuestion() {
        when(questionRepository.save(any(QuestionTemplate.class))).thenReturn(true);
        assertTrue(service.update(question));
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void deleteShouldDeleteQuestion() {
        when(questionRepository.delete(any(QuestionTemplate.class))).thenReturn(true);
        assertTrue(service.delete(question));
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void deleteWithDTOShouldDeleteQuestion() {
        when(questionRepository.delete(any(QuestionTemplate.class))).thenReturn(true);
        when(questionDTO.getQuestion()).thenReturn(question);
        assertTrue(service.delete(questionDTO));
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void getQuestionsForListingShouldReturnTwoQuestionDTOs() {
        HashMap<Integer, QuestionTemplate> map = new HashMap<>();
        map.put(1, question);
        map.put(2, question);
        when(questionRepository.getAll(QuestionTemplate.class)).thenReturn(map);

        assertEquals(service.getQuestionsDataForListing().size(), map.size());
    }
}