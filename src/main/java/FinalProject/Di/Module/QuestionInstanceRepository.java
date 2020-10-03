package FinalProject.Di.Module;

import FinalProject.Domain.Entity.QuestionInstance;
import FinalProject.Repository.Db.Storage.DbStorage;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class QuestionInstanceRepository extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<StorageInterface<QuestionInstance>>() {
        }).to(new TypeLiteral<DbStorage<QuestionInstance>>() {});
    }
}
