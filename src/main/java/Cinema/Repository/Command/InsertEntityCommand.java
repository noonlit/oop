package Cinema.Repository.Command;

import Cinema.Domain.AbstractEntity;
import Cinema.Repository.Storage.StorageInterface;

public class InsertEntityCommand implements ReversibleCommandInterface {
    private final AbstractEntity entity;
    private final StorageInterface storage;

    public InsertEntityCommand(StorageInterface storage, AbstractEntity entity) {
        this.storage = storage;
        this.entity = entity;
    }

    /**
     * Saves the entity to the storage.
     *
     * @return boolean whether the insert was successful.
     */
    public boolean execute() {
        return storage.save(entity);
    }

    /**
     * Undoes the save operation, i.e. deletes an entity.
     *
     * @return whether the deletion was successful.
     */
    public boolean undo() {
        return storage.delete(entity.getId());
    }

    /**
     * Saves the entity to the storage.
     *
     * @return boolean whether the insert was successful.
     */
    public boolean redo() {
        return execute();
    }
}
