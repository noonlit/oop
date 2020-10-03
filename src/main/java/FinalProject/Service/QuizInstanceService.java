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
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class QuizInstanceService {
    private final QuizInstanceValidator quizInstanceValidator;
    private final RepositoryManager repositoryManager;

    @Inject
    public QuizInstanceService(RepositoryManager repositoryManager, QuizInstanceValidator quizInstanceValidator) {
        this.repositoryManager = repositoryManager;
        this.quizInstanceValidator = quizInstanceValidator;
    }

    /**
     * Saves the given quiz instance DTO and its associated questions transactionally.
     */
    public boolean saveSolvedQuiz(@NotNull QuizInstanceDTO quizInstanceDTO) {
        QuizInstance quiz = quizInstanceDTO.getQuiz();
        quizInstanceValidator.validate(quiz);
        return getDefaultRepository().save(quiz, quizInstanceDTO.getQuestions());
    }

    /**
     * Saves the given quiz instance DTO. This is used when the admin updates the score.
     */
    public boolean save(@NotNull QuizInstanceDTO quizInstanceDTO) {
        QuizInstance quiz = quizInstanceDTO.getQuiz();
        quizInstanceValidator.validate(quiz);
        return getDefaultRepository().save(quiz);
    }

    /**
     * Returns a list of quiz data to be displayed for the candidate.
     */
    public Collection<QuizInstanceDTO> getQuizzesDataForUserScoresListing(@NotNull UserDTO currentUser) {
        Map<Integer, QuizInstance> entityMap = getDefaultRepository().findAll(currentUser.getUser());

        List<QuizInstanceDTO> result = new ArrayList<>();
        for (QuizInstance quiz : entityMap.values()) {
            QuizInstanceDTO dto = new QuizInstanceDTO();
            dto.setQuiz(quiz);
            result.add(dto);
        }

        return result;
    }

    /**
     * Returns a collection of questions associated with the given quiz.
     */
    public Collection<QuestionInstance> getAssociatedQuestions(QuizInstance quiz) {
        QuestionInstanceRepository repository = (QuestionInstanceRepository) repositoryManager.getRepository(QuestionInstanceRepository.class);
        return repository.getAssociatedQuestions(quiz).values();
    }

    /**
     * Returns a list of quiz data to be displayed in the admin listing.
     */
    public List<QuizInstanceDTO> getQuizzesDataForAdminListing() {
        HashMap<Integer, QuizInstance> entityMap = getDefaultRepository().getAll(QuizInstance.class);
        UserRepository userRepository = (UserRepository) repositoryManager.getRepository(UserRepository.class);

        List<QuizInstanceDTO> result = new ArrayList<>();
        for (QuizInstance quiz : entityMap.values()) {
            QuizInstanceDTO dto = new QuizInstanceDTO();
            dto.setQuiz(quiz);
            User user = userRepository.findOne(User.class, quiz.getUserId());
            dto.setUser(user);
            result.add(dto);
        }

        return result;
    }

    /**
     * Returns this service's default repository, which is the quiz instance repository.
     */
    private QuizInstanceRepository getDefaultRepository() {
        return (QuizInstanceRepository) repositoryManager.getRepository(QuizInstanceRepository.class);
    }
}
