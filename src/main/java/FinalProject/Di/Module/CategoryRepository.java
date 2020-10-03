package FinalProject.Di.Module;

import FinalProject.Domain.Entity.Category;
import FinalProject.Repository.Db.Storage.DbStorage;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class CategoryRepository extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<StorageInterface<Category>>() {
        }).to(new TypeLiteral<DbStorage<Category>>() {});
    }
}

