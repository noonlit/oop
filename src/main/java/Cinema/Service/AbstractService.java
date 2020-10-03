package Cinema.Service;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Validator.ValidatorInterface;
import Cinema.Repository.AbstractRepository;
import Cinema.Repository.History.HistoryAwareInterface;
import Cinema.Repository.RepositoryManager;

import java.util.List;

/**
 * Provides base functionality for services.
 */
abstract public class AbstractService implements HistoryAwareInterface {
    /**
     * The default repository corresponding to the given service
     * (e.g. the default repository for a MovieService would be a MovieRepository)
     */
    protected AbstractRepository defaultRepository;

    /**
     * Validator for data that passes from the UI to the persistence layer.
     */
    protected ValidatorInterface validator;

    /**
     * Instance that should provide access to any repository in the application.
     */
    protected RepositoryManager repositoryManager;

    public AbstractService(RepositoryManager repositoryManager, ValidatorInterface validator) {
        this.validator = validator;
        this.repositoryManager = repositoryManager;
        this.setDefaultRepository();
    }

    /**
     * Finds a list of entities by the given string.
     * If the query is empty or null, all entities are returned.
     *
     * The searched fields differ for every entity.
     * @param query The query string.
     * @return The results.
     */
    public List<AbstractEntity> find(String query) {
        if (query == null || query.isEmpty()) {
            return defaultRepository.findAll();
        }

        return defaultRepository.find(query);
    }

    public AbstractEntity persist(AbstractEntity entity) {
        defaultRepository.save(entity);
        return entity;
    }

    public boolean delete(int id) {
        return defaultRepository.delete(id);
    }

    public AbstractEntity findOne(String query) {
        return defaultRepository.findOne(query);
    }

    /**
     * Returns a list of entities associated with the service's repository.
     * @return The entities.
     */
    public List<AbstractEntity> findAll() {
        return defaultRepository.findAll();
    }

    @Override
    public boolean undo() {
        return defaultRepository.undo();
    }

    @Override
    public boolean redo() {
        return defaultRepository.redo();
    }

    abstract protected void setDefaultRepository();
}
