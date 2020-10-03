package JavaFX.Domain.Operations;

import JavaFX.Domain.Entity;
import JavaFX.Repository.IRepository;

public class AddOperation<T extends Entity> extends UndoRedoOperation {
    private T addedEntity;

    public AddOperation(IRepository iRepository, T addedEntity) {
        super(iRepository);
        this.addedEntity = addedEntity;
    }

    @Override
    public void undo() {
        this.iRepository.delete(addedEntity.getId());
    }

    @Override
    public void redo() {
        this.iRepository.create(addedEntity);
    }
}
