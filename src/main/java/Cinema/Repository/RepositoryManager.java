package Cinema.Repository;

import Cinema.Exception.RepositoryNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * This class exists to provide Repository instances to Services.
 */
public class RepositoryManager {
   private static Map<String, AbstractRepository> repositoryList = new HashMap<String, AbstractRepository>();

    /**
     * Registers a repository.
     *
     * @param repository The repository.
     */
    public  void addRepository(AbstractRepository repository) {
        repositoryList.put(repository.getClass().getName(), repository);
    }

    /**
     * @param classInstance An instance of the repository's Class.
     * @return The registered repository.
     * @throws RepositoryNotFoundException If the repository isn't registered or doesn't exist.
     */
    public AbstractRepository getRepository(@NotNull Class classInstance) {
        AbstractRepository repository = repositoryList.get(classInstance.getName());

        if (repository == null) {
            throw new RepositoryNotFoundException("The repository doesn't exist or it is not registered!");
        }

        return repository;
    }
}
