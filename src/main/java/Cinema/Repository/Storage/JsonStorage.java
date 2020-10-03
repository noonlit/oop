package Cinema.Repository.Storage;

import Cinema.Exception.CouldNotReadException;
import Cinema.Exception.CouldNotSaveException;
import com.google.gson.Gson;
import Cinema.Domain.AbstractEntity;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonStorage implements StorageInterface {
    protected int lastId = 0;
    private final String path;
    private Type type;
    protected Map<Integer, AbstractEntity> elements = new HashMap<>();

    public JsonStorage(String path, Type type) {
        this.path = path;
        this.type = type;
    }

    @Override
    public boolean entryExists(Integer id) {
        if (id == null) {
            return false;
        }

        if (elements.isEmpty()) {
            loadFromFile();
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
        saveToFile(entity);

        return true;
    }

    @Override
    public boolean saveAll(List<AbstractEntity> list) {
        for (AbstractEntity element : list) {
            save(element);
        }

        return true;
    }

    @Override
    public boolean delete(Integer id) {
        if (!entryExists(id)) {
            return false;
        }

        elements.remove(id);
        saveAllToFile();
        loadFromFile();

        return true;
    }

    @Override
    public boolean deleteAll(List<AbstractEntity> list) {
        for (AbstractEntity element : list) {
            elements.remove(element.getId());
        }

        saveAllToFile();
        loadFromFile();
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

    public void reload() {
        this.loadFromFile();
    }

    private void loadFromFile() {
        elements.clear();
        Gson gson = new Gson();

        // try will automatically also close the opened file
        try (FileReader in = new FileReader(path)) {
            try (BufferedReader bufferedReader = new BufferedReader(in)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    AbstractEntity entity = gson.fromJson(line, type);
                    elements.put(entity.getId(), entity);
                    lastId = entity.getId();
                }
            }
        } catch (Exception e) {
            throw new CouldNotReadException("Could not read from database file!");
        }
    }

    private void saveToFile(AbstractEntity entity)  {
        Gson gson = new Gson();

        try (FileWriter out = new FileWriter(path, true)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(out)) {
                bufferedWriter.write(gson.toJson(entity));
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            throw new CouldNotSaveException("Could not save entity with ID " + entity.getId());
        }
    }

    private void saveAllToFile() {
        Gson gson = new Gson();
        try (FileWriter out = new FileWriter(path)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(out)) {
                for (AbstractEntity element : elements.values()) {
                    bufferedWriter.write(gson.toJson(element));
                    bufferedWriter.newLine();
                }
            }
        } catch (Exception e) {
            throw new CouldNotSaveException("Could not save entities");
        }
    }
}
