package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.UserRole;
import FinalProject.Repository.Db.Storage.Adapter;
import FinalProject.Repository.Db.Storage.QueryBuilderFactory;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.Inject;

public class UserRoleRepository extends AbstractRepository<UserRole> {
    @Inject
    public UserRoleRepository(Adapter adapter, QueryBuilderFactory queryBuilderFactory, StorageInterface<UserRole> storage) {
        super(adapter, queryBuilderFactory, storage);
    }
}
