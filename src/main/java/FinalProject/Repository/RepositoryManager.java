package FinalProject.Repository;

import FinalProject.Di.Di;
import FinalProject.Exception.RepositoryNotFoundException;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class RepositoryManager {
    private static Map<String, RepositoryInterface> repositoryList = new HashMap<>();

    /**
     * Registers a repository.
     *
     * @param repository The repository.
     */
    public void addRepository(RepositoryInterface repository) {
        repositoryList.put(repository.getClass().getName(), repository);
    }

    /**
     * @param classInstance An instance of the repository's Class.
     * @return The registered repository.
     * @throws RepositoryNotFoundException If the repository isn't registered or doesn't exist.
     */
    public RepositoryInterface getRepository(@NotNull Class classInstance) {
        RepositoryInterface repository = repositoryList.get(classInstance.getName());

        if (repository == null) {
            repository = (RepositoryInterface) Di.getInstance(classInstance);
            addRepository(repository);
        }

        return repository;
    }
}
