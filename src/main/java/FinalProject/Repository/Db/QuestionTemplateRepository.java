package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Repository.Db.Storage.Adapter;
import FinalProject.Repository.Db.Storage.QueryBuilder;
import FinalProject.Repository.Db.Storage.QueryBuilderFactory;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class QuestionTemplateRepository extends AbstractRepository<QuestionTemplate> {
    @Inject
    public QuestionTemplateRepository(
            Adapter adapter,
            QueryBuilderFactory queryBuilderFactory,
            StorageInterface<QuestionTemplate> storage
    ) {
        super(adapter, queryBuilderFactory, storage);
    }

    /**
     * Returns all questions that are or can be associated to the given quiz template.
     */
    public HashMap<Integer, QuestionTemplate> getQuestionsAvailableForQuizAssociation(@NotNull QuizTemplate quizTemplate) {
        QueryBuilder queryBuilder = storage.getQueryBuilder();
        queryBuilder.select()
                .from(adapter.getTableName(QuestionTemplate.class))
                .where("quiz_id IS NULL OR quiz_id = ?", quizTemplate.getId());

        return storage.fetchAll(QuestionTemplate.class, queryBuilder);
    }

    /**
     * Returns all questions that are not associated to a quiz.
     */
    public HashMap<Integer, QuestionTemplate> getQuestionsAvailableForQuizAssociation() {
        QueryBuilder queryBuilder = storage.getQueryBuilder();
        queryBuilder.select()
                .from(adapter.getTableName(QuestionTemplate.class))
                .where("quiz_id IS NULL");

        return storage.fetchAll(QuestionTemplate.class, queryBuilder);
    }

    /**
     * Returns all questions that are associated to the given quiz template.
     */
    public HashMap<Integer, QuestionTemplate> getAssociatedQuestions(@NotNull QuizTemplate quizTemplate) {
        QueryBuilder queryBuilder = storage.getQueryBuilder();
        queryBuilder.select()
                .from(adapter.getTableName(QuestionTemplate.class))
                .where("quiz_id = ?", quizTemplate.getId());

        return storage.fetchAll(QuestionTemplate.class, queryBuilder);
    }
}
