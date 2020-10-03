package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.User;
import FinalProject.Repository.Db.Storage.Adapter;
import FinalProject.Repository.Db.Storage.QueryBuilder;
import FinalProject.Repository.Db.Storage.QueryBuilderFactory;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.Inject;

public class UserRepository extends AbstractRepository<User> {
    @Inject
    public UserRepository(Adapter adapter, QueryBuilderFactory queryBuilderFactory, StorageInterface<User> storage) {
        super(adapter, queryBuilderFactory, storage);
    }

    /**
     * Returns the user with the given email.
     */
    public User findOneByEmail(String email) {
        QueryBuilder queryBuilder = storage.getQueryBuilder();
        queryBuilder.select()
                .from(adapter.getTableName(User.class))
                .where("email = ?", email)
                .limit(1);

        return storage.fetchOne(User.class, queryBuilder);
    }
}
