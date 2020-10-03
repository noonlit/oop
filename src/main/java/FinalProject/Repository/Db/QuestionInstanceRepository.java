package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.QuestionInstance;
import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Repository.Db.Storage.Adapter;
import FinalProject.Repository.Db.Storage.QueryBuilder;
import FinalProject.Repository.Db.Storage.QueryBuilderFactory;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class QuestionInstanceRepository extends AbstractRepository<QuestionInstance> {
    @Inject
    public QuestionInstanceRepository(
            Adapter adapter,
            QueryBuilderFactory queryBuilderFactory,
            StorageInterface<QuestionInstance> storage
    ) {
        super(adapter, queryBuilderFactory, storage);
    }

    /**
     * Returns questions associated to the given quiz instance (i.e. questions answered by a candidate).
     */
    public HashMap<Integer, QuestionInstance> getAssociatedQuestions(@NotNull QuizInstance quizInstance) {
        QueryBuilder queryBuilder = storage.getQueryBuilder();
        queryBuilder.select()
                .from(adapter.getTableName(QuestionInstance.class))
                .where("quiz_instance_id = ?", quizInstance.getId());

        return storage.fetchAll(QuestionInstance.class, queryBuilder);
    }
}
