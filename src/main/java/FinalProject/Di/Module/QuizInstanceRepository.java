package FinalProject.Di.Module;

import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Repository.Db.Storage.DbStorage;
import FinalProject.Repository.Db.Storage.StorageInterface;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class QuizInstanceRepository extends AbstractModule {
    protected void configure() {
        bind(new TypeLiteral<StorageInterface<QuizInstance>>() {
        }).to(new TypeLiteral<DbStorage<QuizInstance>>() {});
    }
}
