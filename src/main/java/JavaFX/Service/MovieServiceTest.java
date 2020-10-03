package JavaFX.Service;

import JavaFX.Domain.Movie;
import JavaFX.Domain.Validators.MovieValidator;
import JavaFX.Repository.IRepository;
import JavaFX.Repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieServiceTest {
    IRepository<Movie> movieIRepository = new InMemoryRepository<>();

    MovieValidator movieValidator = new MovieValidator();
    UndoRedoService undoRedoService = new UndoRedoService(movieIRepository, null);
    MovieService movieService = new MovieService(movieIRepository, movieValidator, undoRedoService);
    Movie movie;

    @BeforeEach
    void populate() {
        movie = new Movie("Inception", 2010, 20);

        movieService.addMovie(movie.getTitle(),
                movie.getYearOfRelease(),
                movie.getPrice());

    }

    @Test
    void addingAValidMovie_should_saveThatMovieToRepository() {
        assertEquals(1, movieIRepository.read().size());
        assertEquals(movie, movieIRepository.read(movie.getId()));
        undoRedoService.undo();
        assertEquals(0, movieIRepository.read().size());
        undoRedoService.redo();
        assertEquals(1, movieIRepository.read().size());
        assertEquals(movie, movieIRepository.read(movie.getId()));

    }

    @Test
    void addingPriceIncrease_should_increaseOnlyTheMoviesPricesLowerThanGivenNumber(){
        int increase = 10;
        this.movieService.priceIncrease(increase,0);
        assertEquals(30,movieIRepository.read(movie.getId()).getPrice());
        this.movieService.priceIncrease(increase,100);
        assertEquals(30,movieIRepository.read(movie.getId()).getPrice());
        undoRedoService.undo();
        undoRedoService.undo();
        assertEquals(20,movieIRepository.read(movie.getId()).getPrice());
        undoRedoService.redo();
        assertEquals(30,movieIRepository.read(movie.getId()).getPrice());
    }

    @Test
    void addingPriceDecrease_should_decreaseOnlyTheMoviesPricesLowerThanGivenNumber(){
        int decrease = 10;
        this.movieService.priceDecrease(decrease,0);
        assertEquals(10,movieIRepository.read(movie.getId()).getPrice());
        this.movieService.priceDecrease(decrease,100);
        assertEquals(10,movieIRepository.read(movie.getId()).getPrice());
        undoRedoService.undo();
        undoRedoService.undo();
        assertEquals(20,movieIRepository.read(movie.getId()).getPrice());
        undoRedoService.redo();
        assertEquals(10,movieIRepository.read(movie.getId()).getPrice());
    }

    @Test
    void getAll_should_returnAListOfMovies(){
        Movie movie1=new Movie(movie);
        Movie movie2=new Movie(movie);
        this.movieIRepository.create(movie1);
        this.movieIRepository.create(movie2);
        List<Movie> test = new ArrayList<>();
        test.add(movie);
        test.add(movie1);
        test.add(movie2);
        assertEquals(test,this.movieService.getAll());
    }
}