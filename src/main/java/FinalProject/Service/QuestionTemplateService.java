package FinalProject.Service;

import FinalProject.Domain.DTO.QuestionTemplateDTO;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Validator.QuestionValidator;
import FinalProject.Repository.Db.QuestionTemplateRepository;
import FinalProject.Repository.RepositoryManager;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionTemplateService {
    private final QuestionValidator questionValidator;
    private final RepositoryManager repositoryManager;

    @Inject
    public QuestionTemplateService(RepositoryManager repositoryManager, QuestionValidator questionValidator) {
        this.repositoryManager = repositoryManager;
        this.questionValidator = questionValidator;
    }

    /**
     * Returns this service's default repository, which is the question template repository.
     */
    private QuestionTemplateRepository getDefaultRepository() {
        return (QuestionTemplateRepository) repositoryManager.getRepository(QuestionTemplateRepository.class);
    }

    /**
     * Retrieves a list of question DTOs to be displayed on the question listing page.
     */
    public List<QuestionTemplateDTO> getQuestionsDataForListing() {
        HashMap<Integer, QuestionTemplate> questions = getDefaultRepository().getAll(QuestionTemplate.class);

        /*
         * Build DTOs containing the question data.
         *
         * Note: here we could return the questions themselves,
         * but we might need to add more data depending on what the listing displays
         */
        List<QuestionTemplateDTO> questionTemplateList = new ArrayList<>();
        for (Map.Entry<Integer, QuestionTemplate> entry : questions.entrySet()) {
            QuestionTemplate question = entry.getValue();
            QuestionTemplateDTO appQuestion = new QuestionTemplateDTO();
            appQuestion.setQuestion(question);
            questionTemplateList.add(appQuestion);
        }

        return questionTemplateList;
    }

    /**
     * Deletes the question wrapped in the given DTO.
     */
    public boolean delete(@NotNull QuestionTemplateDTO questionDTO) {
        return delete(questionDTO.getQuestion());
    }

    /**
     * Deletes the given question.
     */
    public boolean delete(@NotNull QuestionTemplate question) {
        return getDefaultRepository().delete(question);
    }

    public boolean update(@NotNull QuestionTemplate question) {
        return getDefaultRepository().save(question);
    }

    public boolean save(QuestionTemplate question) {
        questionValidator.validate(question);
        return getDefaultRepository().save(question);
    }
}
