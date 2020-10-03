package Cinema.Repository.Command;

import Cinema.Domain.AbstractEntity;
import Cinema.Repository.Storage.StorageInterface;

public class UpdateEntityCommand implements ReversibleCommandInterface {
    private final AbstractEntity entity;
    private AbstractEntity previousEntity;
    private final StorageInterface storage;

    public UpdateEntityCommand(StorageInterface storage, AbstractEntity entity) {
        this.storage = storage;
        this.entity = entity;
    }

    /**
     * Updates the given entity.
     * Saves a clone locally to enable us to revert the operation.
     *
     * @return whether the update was successful
     */
    public boolean execute() {
        previousEntity = storage.get(entity.getId()).clone();
        return storage.save(entity);
    }

    /**
     * Reverts the update, i.e. saves the entity clone to the storage instead of the entity.
     *
     * @return whether the update was successful
     */
    public boolean undo() {
        return storage.save(previousEntity);
    }

    /**
     * Updates the given entity.
     *
     * @return whether the update was successful
     */
    public boolean redo() {
        return execute();
    }
}
