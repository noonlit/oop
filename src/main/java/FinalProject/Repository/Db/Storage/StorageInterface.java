package FinalProject.Repository.Db.Storage;

import FinalProject.Domain.Entity.AbstractEntity;

import java.util.HashMap;

public interface StorageInterface<E extends AbstractEntity> {
    Adapter getAdapter();

    QueryBuilder getQueryBuilder();

    boolean save(E entity);

    boolean delete(Class<E> entityClass, Integer id);

    E fetchOne(Class<E> entityClass, QueryBuilder queryBuilder);

    E fetchOne(Class<E> entityClass, int id);

    HashMap<Integer, E> fetchAll(Class<E> entityClass, QueryBuilder queryBuilder);

    HashMap<Integer, E> fetchAll(Class<E> entityClass);
}
