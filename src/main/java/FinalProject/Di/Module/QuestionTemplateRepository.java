package FinalProject.Di.Module;

import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Repository.Db.Storage.DbStorage;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class QuestionTemplateRepository extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<StorageInterface<QuestionTemplate>>() {
        }).to(new TypeLiteral<DbStorage<QuestionTemplate>>() {});
    }
}
