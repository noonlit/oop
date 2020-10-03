package FinalProject.Di.Module;

import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Repository.Db.Storage.DbStorage;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class QuizTemplateRepository extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<StorageInterface<QuizTemplate>>() {
        }).to(new TypeLiteral<DbStorage<QuizTemplate>>() {});
    }
}
