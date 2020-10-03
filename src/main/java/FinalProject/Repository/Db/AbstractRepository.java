package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.AbstractEntity;
import FinalProject.Repository.Db.Storage.Adapter;
import FinalProject.Repository.Db.Storage.QueryBuilderFactory;
import FinalProject.Repository.Db.Storage.StorageInterface;
import FinalProject.Repository.RepositoryInterface;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

abstract public class AbstractRepository<E extends AbstractEntity> implements RepositoryInterface<E> {
    protected final QueryBuilderFactory queryBuilderFactory;
    protected final StorageInterface<E> storage;
    protected final Adapter adapter;

    @Inject
    public AbstractRepository(Adapter adapter, QueryBuilderFactory queryBuilderFactory, StorageInterface<E> storage) {
        this.adapter = adapter;
        this.queryBuilderFactory = queryBuilderFactory;
        this.storage = storage;
    }

    /**
     * Saves the given entity data to the database.
     */
    public boolean save(@NotNull E entity) {
        return storage.save(entity);
    }

    /**
     * Deletes the given entity data from the database.
     */
    public boolean delete(@NotNull E entity) {
        return storage.delete((Class<E>) entity.getClass(), entity.getId());
    }

    /**
     * Deletes the row corresponding to the entity with the given ID from the database.
     */
    public boolean deleteById(Class<E> entityClass, int id) {
        return storage.delete(entityClass, id);
    }

    /**
     * Retrieves an entity of the given type, hydrated with data from the database.
     */
    public E findOne(Class<E> entityClass, int id) {
        return storage.fetchOne(entityClass, id);
    }

    /**
     * Retrieves a map of entities of the given type, hydrated with data from the database.
     */
    public HashMap<Integer, E> getAll(Class<E> entityClass) {
        return storage.fetchAll(entityClass);
    }
}
