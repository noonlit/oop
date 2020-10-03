package JavaFX.Domain.Operations;

import JavaFX.Domain.Exceptions.InvalidMovieException;
import JavaFX.Domain.Movie;
import JavaFX.Repository.IRepository;

public class AddPrinceIncrease extends UndoRedoOperation<Movie> {
    private int increase;
    private int price;
    public AddPrinceIncrease(IRepository iRepository, int increase, int price) {
        super(iRepository);
        this.increase = increase;
        this.price = price;
    }

    @Override
    public void undo() {
        for (Movie movie : this.iRepository.read()) {
            if (price < movie.getPrice()) {
                movie.setPrice(movie.getPrice() - increase);
                if (movie.getPrice() < 0) throw new InvalidMovieException("The price can't be negative");
                this.iRepository.update(movie);
            }
        }

    }

    @Override
    public void redo() {
        for (Movie movie : this.iRepository.read()) {
            if (price < movie.getPrice()) {
                movie.setPrice(movie.getPrice() + increase);
                this.iRepository.update(movie);
            }
        }

    }
}
