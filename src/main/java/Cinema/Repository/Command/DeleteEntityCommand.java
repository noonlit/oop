package Cinema.Repository.Command;

import Cinema.Domain.AbstractEntity;
import Cinema.Repository.Storage.StorageInterface;

/**
 * Deletes an entity.
 */
public class DeleteEntityCommand implements ReversibleCommandInterface {
    private final StorageInterface storage;
    private AbstractEntity entity;
    private final int id;

    public DeleteEntityCommand(StorageInterface storage, int id) {
        this.storage = storage;
        this.id = id;
    }

    /**
     * Deletes the entity from storage.
     *
     * @return whether the entity was deleted.
     */
    public boolean execute() {
        entity = storage.get(id);

        if (entity == null) {
            return false;
        }

        return storage.delete(id);
    }

    /**
     * Undoes the deletion, i.e. saves the entity to storage.
     *
     * @return whether the deletion was successfully reversed.
     */
    public boolean undo() {
        return storage.save(entity);
    }

    /**
     * Deletes the entity from storage.
     *
     * @return whether the deletion was successfully redone.
     */
    public boolean redo() {
        return execute();
    }
}
