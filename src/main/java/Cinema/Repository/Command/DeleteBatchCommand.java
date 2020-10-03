package Cinema.Repository.Command;

import Cinema.Domain.AbstractEntity;
import Cinema.Repository.Storage.StorageInterface;

import java.util.List;

public class DeleteBatchCommand implements ReversibleCommandInterface {
    private final StorageInterface storage;
    private List<AbstractEntity> entityList;

    public DeleteBatchCommand(StorageInterface storage, List<AbstractEntity> entityList) {
        this.storage = storage;
        this.entityList = entityList;
    }

    /**
     * Deletes the entities from storage.
     *
     * @return whether the entities were deleted.
     */
    public boolean execute() {
        return storage.deleteAll(entityList);
    }

    /**
     * Undoes the deletion, i.e. saves the entities to storage.
     *
     * @return whether the deletion was successfully reversed.
     */
    public boolean undo() {
        return storage.saveAll(entityList);
    }

    /**
     * Deletes the entities from storage.
     *
     * @return whether the deletion was successfully redone.
     */
    public boolean redo() {
        return execute();
    }
}
