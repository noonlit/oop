package FinalProject.Repository.Db.Storage;

import FinalProject.Domain.Entity.AbstractEntity;
import FinalProject.Exception.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

@Singleton
public class DbStorage<E extends AbstractEntity> implements StorageInterface<E> {
    private final Adapter adapter;
    private final Hydrator<E> hydrator;
    private final QueryBuilderFactory queryBuilderFactory;

    @Override
    public QueryBuilder getQueryBuilder() {
        return queryBuilderFactory.create();
    }

    @Override
    public Adapter getAdapter() {
        return adapter;
    }

    @Inject
    public DbStorage(Adapter adapter, Hydrator<E> hydrator, QueryBuilderFactory queryBuilderFactory) {
        this.adapter = adapter;
        this.hydrator = hydrator;
        this.queryBuilderFactory = queryBuilderFactory;
    }

    @Override
    public boolean delete(Class<E> entityClass, Integer id) {
        try {
            String sql = "DELETE FROM " + adapter.getTableName(entityClass) + " WHERE id = ?";

            PreparedStatement statement = adapter.prepare(sql, id);
            return adapter.executeDML(statement) == 1;
        } catch (Exception e) {
            throw new EntityNotDeletedException("Could not delete the entity!");
        }
    }

    @Override
    public boolean save(@NotNull E entity) {
        if (entity.getId() == null) {
            return insert(entity);
        }

        return update(entity);
    }

    protected boolean insert(@NotNull E entity) {
        HashMap<String, Object> fields = hydrator.extract(entity);
        String insert = adapter.buildInsert(adapter.getTableName(entity.getClass()), fields);

        try {
            PreparedStatement statement = adapter.prepareInsert(insert, fields.values());
            int result = adapter.executeDML(statement);

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Integer key = generatedKeys.getInt(1);
                entity.setId(key);
            }

            return result == 1;
        } catch (Exception e) {
            throw new EntityNotSavedException("Could not save the entity!");
        }
    }

    protected boolean update(E entity) {
        HashMap<String, Object> fields = hydrator.extract(entity);
        String update = adapter.buildUpdate(adapter.getTableName(entity.getClass()), fields);

        // restrict updates to entity with this ID
        update += " WHERE id = ?";

        try {
            Collection<Object> values = fields.values();
            PreparedStatement statement = adapter.prepare(update, values);

            // bind ID
            statement.setInt(values.size() + 1, entity.getId());
            return adapter.executeDML(statement) == 1;
        } catch (Exception e) {
            throw new EntityNotSavedException("Could not update the entity!");
        }
    }

    @Override
    public E fetchOne(Class<E> entityClass, int id) {
        ResultSet result;

        try {
            QueryBuilder queryBuilder = queryBuilderFactory.create();
            queryBuilder.select()
                    .from(adapter.getTableName(entityClass))
                    .where("id = ?", id);

            result = adapter.executeBuiltQuery(queryBuilder);
        } catch (Exception e) {
            throw new CannotExecuteQueryException("Could not execute query!");
        }

        return hydrateOne(entityClass, result);
    }

    @Override
    public HashMap<Integer, E> fetchAll(Class<E> entityClass, QueryBuilder queryBuilder) {
        ResultSet result;

        try {
            result = adapter.executeBuiltQuery(queryBuilder);
            return hydrateAll(entityClass, result);
        } catch (Exception e) {
            throw new CannotExecuteQueryException("Could not execute query!");
        }
    }

    @Override
    public HashMap<Integer, E> fetchAll(Class<E> entityClass) {
        QueryBuilder queryBuilder = queryBuilderFactory.create();
        queryBuilder.select().from(adapter.getTableName(entityClass));

        ResultSet result;
        try {
            result = adapter.executeBuiltQuery(queryBuilder);
        } catch (Exception e) {
            throw new CannotExecuteQueryException("Could not execute query!");
        }

        return hydrateAll(entityClass, result);
    }

    @Override
    public E fetchOne(Class<E> entityClass, QueryBuilder queryBuilder) {
        ResultSet result;
        try {
            result = adapter.executeBuiltQuery(queryBuilder);
        } catch (Exception e) {
            throw new CannotExecuteQueryException("Could not execute query!");
        }

        return hydrateOne(entityClass, result);
    }

    /**
     * Returns an object built from the first row of the given result set.
     */
    protected E hydrateOne(Class<E> entityClass, @NotNull ResultSet resultSet) {
        try (resultSet) {
            if (!resultSet.next()) {
                throw new EntityNotFoundException("The entity was not found!");
            }

            return hydrator.hydrate(entityClass, resultSet);
        } catch (SQLException e) {
            throw new CannotHydrateException("Could not build the entity!");
        }
    }

    /**
     * Returns a map of objects built from the given result set.
     * The key is the row ID.
     */
    protected HashMap<Integer, E> hydrateAll(Class<E> entityClass, @NotNull ResultSet resultSet) {
        HashMap<Integer, E> result = new HashMap<>();

        try (resultSet) {
            while (resultSet.next()) {
                E entity = hydrator.hydrate(entityClass, resultSet);
                result.put(entity.getId(), entity);
            }
        } catch (SQLException e) {
            throw new CannotHydrateException("Could not build the entities!");
        }

        return result;
    }
}
