package JavaFX.Service;

import JavaFX.Domain.Operations.AddOperation;
import JavaFX.Domain.Operations.AddPriceDecrease;
import JavaFX.Domain.Operations.AddPrinceIncrease;
import JavaFX.Domain.Exceptions.InvalidMovieException;
import JavaFX.Domain.Movie;
import JavaFX.Domain.Validators.MovieValidator;
import JavaFX.Repository.IRepository;

import java.util.List;

public class MovieService {

    private IRepository<Movie> movieIRepository;
    private MovieValidator movieValidator;
    private UndoRedoService undoRedoService;


    public MovieService(IRepository<Movie> movieIRepository, MovieValidator movieValidator,
                        UndoRedoService undoRedoService) {
        this.movieIRepository = movieIRepository;
        this.movieValidator = movieValidator;
        this.undoRedoService = undoRedoService;

    }

    public void addMovie(String title, int yearOfRelease, int price) {
        Movie movie = new Movie(title, yearOfRelease, price);
        movieValidator.validate(movie);
        this.movieIRepository.create(movie);
        this.undoRedoService.add(new AddOperation<Movie>(this.movieIRepository, movie));

    }

    public void priceIncrease(int increase, int price) {
        for (Movie movie : this.movieIRepository.read()) {
            if (price < movie.getPrice()) {
                movie.setPrice(movie.getPrice() + increase);
                this.movieIRepository.update(movie);
            }
        }
        this.undoRedoService.add(new AddPrinceIncrease(movieIRepository,increase, price));

    }

    public void priceDecrease(int decrease, int price) {
        for (Movie movie : this.movieIRepository.read()) {
            if (price < movie.getPrice()) {
                movie.setPrice(movie.getPrice() - decrease);
                if (movie.getPrice() < 0) throw new InvalidMovieException("The price can't be negative");
                this.movieIRepository.update(movie);
            }
        }
        this.undoRedoService.add(new AddPriceDecrease(movieIRepository,decrease,price));

    }


    public List<Movie> getAll() {
        return this.movieIRepository.read();
    }
}
