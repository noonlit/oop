package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Domain.Entity.User;
import FinalProject.Exception.TransactionNotCompletedException;
import FinalProject.Repository.Db.Storage.*;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class QuizTemplateRepository extends AbstractRepository<QuizTemplate> {
    @Inject
    public QuizTemplateRepository(
            Adapter adapter,
            QueryBuilderFactory queryBuilderFactory,
            StorageInterface<QuizTemplate> storage
    ) {
        super(adapter, queryBuilderFactory, storage);
    }

    /**
     * Returns quizzes that the given user has not taken yet and have at least one question.
     */
    public Map<Integer, QuizTemplate> getAllQuizzesAvailableForUser(@NotNull User user) {
        QueryBuilder queryBuilder = storage.getQueryBuilder();

        String userSubselect = "SELECT quiz_template_id FROM " +
                adapter.getTableName(QuizInstance.class) +
                " WHERE user_id = ? AND quiz_template_id IS NOT NULL";

        String questionSubselect = "SELECT quiz_id FROM " +
                adapter.getTableName(QuestionTemplate.class) +
                " WHERE quiz_id IS NOT NULL";

        queryBuilder.select()
                .from(adapter.getTableName(QuizTemplate.class))
                .where("id NOT IN (" + userSubselect + ")", user.getId())
                .where("id IN (" + questionSubselect + ")");

        return storage.fetchAll(QuizTemplate.class, queryBuilder);
    }

    /**
     * Saves the given quiz template and associated question templates transactionally.
     */
    public boolean update(QuizTemplate quiz, List<QuestionTemplate> questions) {
        Adapter adapter = storage.getAdapter();
        Connection connection = adapter.getConnection();

        try {
            try {
                connection.setAutoCommit(false);

                super.save(quiz);

                for (QuestionTemplate question : questions) {
                    DbStorage<QuestionTemplate> storage = new DbStorage<>(adapter, new Hydrator<>(), new QueryBuilderFactory());
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
     * Saves the given quiz template and associated question templates transactionally.
     */
    public boolean create(QuizTemplate quiz, List<QuestionTemplate> questions) {
        Adapter adapter = storage.getAdapter();
        Connection connection = adapter.getConnection();

        try {
            try {
                connection.setAutoCommit(false);

                super.save(quiz);

                for (QuestionTemplate question : questions) {
                    DbStorage<QuestionTemplate> storage = new DbStorage<>(adapter, new Hydrator<>(), new QueryBuilderFactory());
                    question.setQuizId(quiz.getId());
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
}
