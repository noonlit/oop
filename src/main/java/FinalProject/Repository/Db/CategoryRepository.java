package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.Category;
import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Repository.Db.Storage.Adapter;
import FinalProject.Repository.Db.Storage.QueryBuilder;
import FinalProject.Repository.Db.Storage.QueryBuilderFactory;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class CategoryRepository extends AbstractRepository<Category> {
    @Inject
    public CategoryRepository(Adapter adapter, QueryBuilderFactory queryBuilderFactory, StorageInterface<Category> storage) {
        super(adapter, queryBuilderFactory, storage);
    }

    /**
     * Returns the category that the given quiz belongs to.
     */
    public Category getCategoryForQuiz(@NotNull QuizTemplate quiz) {
        QueryBuilder queryBuilder = storage.getQueryBuilder();
        queryBuilder.select()
                .from(adapter.getTableName(Category.class))
                .where("id = ?", quiz.getCategoryId());

        return storage.fetchOne(Category.class, queryBuilder);
    }
}
