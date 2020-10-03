package JavaFX.Repository;

import JavaFX.Domain.Entity;
import JavaFX.Domain.Exceptions.WrongIdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository<T extends Entity> implements IRepository<T> {
    private Map<Integer, T> storage = new HashMap<>();
    private int currentId;

    @Override
    public void create(T entity) {
        if (storage.isEmpty()) {
            currentId = 0;
            entity.setId(currentId);
            currentId++;
        }else {
            entity.setId(currentId);
            currentId++;
        }

        storage.put(entity.getId(), entity);
    }
    @Override
    public void recreate(T entity) {

        storage.put(entity.getId(), entity);
    }

    @Override
    public T read(int entityId) {
        return storage.get(entityId);
    }

    @Override
    public List<T> read() {
        List<T> results = new ArrayList<>();
        for (T entity : storage.values()) {
            results.add(entity);
        }
        return results;
    }

    @Override
    public void update(T entity) {
        if (!storage.containsKey(entity.getId())) {
            throw new WrongIdException("this movie ID does not exists!");
        }
        storage.put(entity.getId(), entity);

    }

    @Override
    public void delete(int entityId) {
        if (!storage.containsKey(entityId)) {
            throw new WrongIdException("this movie ID does not exists!");
        }
        storage.remove(entityId);


    }
}
