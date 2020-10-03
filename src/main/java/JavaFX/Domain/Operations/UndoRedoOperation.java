package JavaFX.Domain.Operations;

import JavaFX.Domain.Entity;
import JavaFX.Repository.IRepository;

public abstract class UndoRedoOperation<T extends Entity> {
    protected IRepository<T> iRepository;

    public UndoRedoOperation(IRepository<T> iRepository) {
        this.iRepository = iRepository;
    }

    public abstract void undo();
    public abstract void redo();
}
