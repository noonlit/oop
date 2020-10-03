package FinalProject.Di.Module;

import FinalProject.Domain.Entity.UserRole;
import FinalProject.Repository.Db.Storage.DbStorage;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class UserRoleRepository extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<StorageInterface<UserRole>>() {
        }).to(new TypeLiteral<DbStorage<UserRole>>() {});
    }
}