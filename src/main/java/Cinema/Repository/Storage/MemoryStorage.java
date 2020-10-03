package Cinema.Repository.Storage;

import Cinema.Domain.AbstractEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryStorage implements StorageInterface {
    protected int lastId = 0;
    protected Map<Integer, AbstractEntity> elements = new HashMap<>();

    @Override
    public boolean entryExists(Integer id) {
        if (id == null) {
            return false;
        }

        return this.elements.containsKey(id);
    }

    @Override
    public boolean save(AbstractEntity entity) {
        if (entity.getId() == null) {
            lastId++;
            entity.setId(lastId);
        }

        elements.put(entity.getId(), entity);
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        if (!entryExists(id)) {
            return false;
        }

        elements.remove(id);
        return true;
    }

    public boolean deleteAll(List<AbstractEntity> list) {
        for (AbstractEntity element : list) {
            delete(element.getId());
        }

        return true;
    }

    public boolean saveAll(List<AbstractEntity> list) {
        for (AbstractEntity element : list) {
            save(element);
        }

        return true;
    }

    @Override
    public AbstractEntity get(int id) {
        return elements.get(id);
    }

    @Override
    public List<AbstractEntity> findAll() {
        return new ArrayList<>(elements.values());
    }
}
