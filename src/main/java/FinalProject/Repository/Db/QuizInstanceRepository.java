package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.QuestionInstance;
import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Domain.Entity.User;
import FinalProject.Exception.TransactionNotCompletedException;
import FinalProject.Repository.Db.Storage.*;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class QuizInstanceRepository extends AbstractRepository<QuizInstance> {
    @Inject
    public QuizInstanceRepository(
            Adapter adapter,
            QueryBuilderFactory queryBuilderFactory,
            StorageInterface<QuizInstance> storage
    ) {
        super(adapter, queryBuilderFactory, storage);
    }

    /**
     * Saves the given quiz instance and list of question instances transactionally.
     */
    public boolean save(QuizInstance quiz, List<QuestionInstance> questions) {
        Adapter adapter = storage.getAdapter();
        Connection connection = adapter.getConnection();
        try {
            try (connection) {
                connection.setAutoCommit(false);

                super.save(quiz);

                for (QuestionInstance question : questions) {
                    question.setQuizInstanceId(quiz.getId());
                    DbStorage<QuestionInstance> storage = new DbStorage<>(adapter, new Hydrator<>(), new QueryBuilderFactory());
                    question.setQuizInstanceId(quiz.getId());
                    storage.save(question);
                }

                connection.commit();
                connection.setAutoCommit(true);
            } catch (Exception e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
        } catch (Exception e) {
            throw new TransactionNotCompletedException("Could not save entities transactionally!");
        }

        return true;
    }

    /**
     * Returns all quizzes that the given user has taken.
     */
    public Map<Integer, QuizInstance> findAll(@NotNull User user) {
        QueryBuilder queryBuilder = storage.getQueryBuilder();

        queryBuilder.select()
                .from(adapter.getTableName(QuizInstance.class))
                .where("user_id = ?", user.getId());

        return storage.fetchAll(QuizInstance.class, queryBuilder);
    }
}
