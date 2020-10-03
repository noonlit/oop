package Cinema.Repository.Storage;

import Cinema.Domain.AbstractEntity;

import java.util.List;

/**
 * Interface to be implemented by all types of storage.
 */
public interface StorageInterface {
    /**
     * @param id The entity ID
     * @return whether the entity exists in the storage.
     */
    boolean entryExists(Integer id);

    /**
     * @param entity The entity to save
     * @return whether the save was successful.
     */
    boolean save(AbstractEntity entity);

    /**
     * @param list The entities to save
     * @return whether the save was successful.
     */
    boolean saveAll(List<AbstractEntity> list);

    /**
     * @param id The entity ID
     * @return whether the deletion was successful.
     */
    boolean delete(Integer id);

    /**
     * @param list The entities to delete
     * @return whether the deletion was successful.
     */
    boolean deleteAll(List<AbstractEntity> list);

    /**
     * @param id The entity ID
     * @return The entity
     */
    AbstractEntity get(int id);

    /**
     * @return All entities currently in storage.
     */
    List<AbstractEntity> findAll();
}
