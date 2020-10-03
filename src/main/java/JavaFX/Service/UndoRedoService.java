package JavaFX.Service;

import JavaFX.Domain.Entity;
import JavaFX.Domain.Movie;
import JavaFX.Domain.MovieReservation;
import JavaFX.Domain.Operations.UndoRedoOperation;
import JavaFX.Repository.IRepository;

import java.util.Stack;

public class UndoRedoService  {
    private IRepository<Movie> movieIRepository;
    private IRepository<MovieReservation> movieReservationIRepository;
    private Stack<UndoRedoOperation<? extends Entity>> undoStack;
    private Stack<UndoRedoOperation<? extends Entity>> redoStack;
    public UndoRedoService(IRepository<Movie> movieIRepository,
                           IRepository<MovieReservation> movieReservationIRepository) {
        this.movieIRepository = movieIRepository;
        this.movieReservationIRepository = movieReservationIRepository;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void add(UndoRedoOperation<? extends Entity> operation){
        this.undoStack.push(operation);
        this.redoStack.clear();
    }

    public boolean undo() {
        if (!this.undoStack.isEmpty()) {
            UndoRedoOperation<? extends Entity> movieUndoRedoOperation = this.undoStack.pop();
            movieUndoRedoOperation.undo();
            this.redoStack.push(movieUndoRedoOperation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if(!this.redoStack.isEmpty()){
            UndoRedoOperation<? extends Entity> movieUndoRedoOperation = this.redoStack.pop();
            movieUndoRedoOperation.redo();
            this.undoStack.push(movieUndoRedoOperation);
            return true;
        }
        return false;
    }

}
