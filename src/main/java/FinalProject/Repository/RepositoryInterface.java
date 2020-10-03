package FinalProject.Repository;

import FinalProject.Domain.Entity.AbstractEntity;

import java.util.HashMap;

public interface RepositoryInterface<E extends AbstractEntity> {
    /**
     * Saves the given entity.
     */
    boolean save(E entity);

    /**
     * Returns an entity of the given class with the given ID.
     */
    E findOne(Class<E> entityClass, int id);

    /**
     * Returns a map of entities. The key is the entity ID.
     */
    HashMap<Integer, E> getAll(Class<E> entityClass);

    /**
     * Deletes a given entity.
     */
    boolean delete(E entity);

    /**
     * Deletes an entity of the given class by ID.
     */
    boolean deleteById(Class<E> entityClass, int id);
}
