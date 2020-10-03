package FinalProject.Di.Module;

import FinalProject.Domain.Entity.User;
import FinalProject.Repository.Db.Storage.DbStorage;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class UserRepository extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<StorageInterface<User>>() {
        }).to(new TypeLiteral<DbStorage<User>>() {});
    }
}
