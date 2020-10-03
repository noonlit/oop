package Cinema.Repository;

import Cinema.Domain.AbstractEntity;
import Cinema.Repository.Command.*;
import Cinema.Repository.History.History;
import Cinema.Repository.History.HistoryAwareInterface;
import Cinema.Repository.Storage.StorageInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

abstract public class AbstractRepository implements HistoryAwareInterface {
    protected History undoLog;
    protected History redoLog;
    protected StorageInterface storage;

    /**
     * Constructor.
     * @param undoLog keeps track of commands (create, insert, update) to undo.
     * @param redoLog keeps track of commands (create, insert, update) to redo.
     * @param storage where we keep The data (e.g. in memory, db, files)
     */
    public AbstractRepository(History undoLog, History redoLog, StorageInterface storage) {
        this.undoLog = undoLog;
        this.redoLog = redoLog;
        this.storage = storage;
    }

    /**
     * Saves the given entity to storage.
     *
     * @param entity The entity to save.
     * @return boolean whether the save occurred.
     */
    public boolean save(@NotNull AbstractEntity entity) {
        if (!storage.entryExists(entity.getId())) {
            return insert(entity);
        }

        return update(entity);
    }

    /**
     * Saves the given entity to storage.
     * Keeps track of insertion so that we are able to revert it.
     *
     * @param entity The entity to insert.
     * @return boolean whether the insert occurred.
     */
    private boolean insert(@NotNull AbstractEntity entity) {
        ReversibleCommandInterface command = new InsertEntityCommand(storage, entity);
        undoLog.recordCommand(command);

        return command.execute();
    }

    /**
     * Updates the given entity.
     * Keeps track of update so that we are able to revert it.
     *
     * @param entity The entity to update.
     * @return boolean whether the update occurred.
     */
    private boolean update(@NotNull AbstractEntity entity) {
        ReversibleCommandInterface command = new UpdateEntityCommand(storage, entity);
        undoLog.recordCommand(command);

        return command.execute();
    }

    /**
     * Deletes the entity with the given id.
     * Keeps track of deletion so that we are able to revert it.
     *
     * @param id The entity id
     * @return whether the deletion occurred.
     */
    public boolean delete(int id) {
        ReversibleCommandInterface command = new DeleteEntityCommand(storage, id);
        undoLog.recordCommand(command);

        return command.execute();
    }

    /**
     * Checks whether the entity with the given ID exists.
     *
     * @param id
     * @return
     */
    public boolean entryExists(int id) {
        return storage.entryExists(id);
    }

    /**
     * Returns an entity with the given id.
     * The entity is a clone of the one found in storage,
     * because we want to avoid accidentally implementing the Active Record pattern :)
     *
     * @param id The entity ID
     * @return A clone of the entity or null if not found.
     */
    public AbstractEntity get(int id) {
        AbstractEntity entity = storage.get(id);

        if (entity != null) {
            return entity.clone();
        }

        return null;
    }

    /**
     * Returns a list of all entities found in storage.
     * The entities are clones of the actual stored objects.
     *
     * @return The list of entities.
     */
    public List<AbstractEntity> findAll() {
        List<AbstractEntity> elements = storage.findAll();

        return elements.stream()
                .map(AbstractEntity::clone)
                .collect(Collectors.toList());
    }

    public boolean deleteAll(List<AbstractEntity> entities) {
        ReversibleCommandInterface command = new DeleteBatchCommand(storage, entities);
        undoLog.recordCommand(command);

        return command.execute();
    }

    /**
     * Reverts the last operation.
     * Keeps track of reversion so that we can redo the operation.
     *
     * @return whether the operation was successfully reverted.
     */
    public boolean undo() {
        ReversibleCommandInterface savedCommand = undoLog.getLastCommand();

        if (savedCommand == null) {
            return false;
        }

        redoLog.recordCommand(savedCommand);
        return savedCommand.undo();
    }

    /**
     * Redoes the last reverted operation.
     * Keeps track of the command so that we can revert it (again).
     *
     * @return whether the operation was successfully re-executed.
     */
    public boolean redo() {
        ReversibleCommandInterface savedCommand = redoLog.getLastCommand();

        if (savedCommand == null) {
            return false;
        }

        undoLog.recordCommand(savedCommand);
        return savedCommand.redo();
    }

    public StorageInterface getStorage() {
        return storage;
    }

    public abstract List<AbstractEntity> find(String query);
    public abstract AbstractEntity findOne(String query);
}
