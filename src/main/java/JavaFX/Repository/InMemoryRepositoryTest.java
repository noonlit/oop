package JavaFX.Repository;

import JavaFX.Domain.Movie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryRepositoryTest {
    @Test
    void CreatingMovie_should_saveThatMovieToRepository() {
        IRepository<Movie> movieIRepository = new InMemoryRepository<>();
        Movie movie = new Movie( "Inception", 2010, 20);
        movieIRepository.create(movie);
        assertEquals(1, movieIRepository.read().size());

    }

    @Test
    void ReadingIdMovie_should_returnThatMovie() {
        IRepository<Movie> movieIRepository = new InMemoryRepository<>();
        Movie movie = new Movie( "Inception", 2010, 20);
        movieIRepository.create(movie);
        assertEquals(1, movieIRepository.read().size());
        assertEquals(movie, movieIRepository.read(movie.getId()));
    }

    @Test
    void ReadingAllMovies_should_returnAListOfMovies() {
        IRepository<Movie> movieIRepository = new InMemoryRepository<>();
        Movie movie = new Movie( "Inception", 2010, 20);
        Movie movie2 = new Movie( "Batman", 1989, 20);
        List<Movie> test = new ArrayList<>();
        test.add(movie);
        test.add(movie2);
        movieIRepository.create(movie);
        movieIRepository.create(movie2);
        assertEquals(2, movieIRepository.read().size());
        assertEquals(test, movieIRepository.read());

    }

    @Test
    void DeleteMovie_should_DeleteThatMovieFromRepository() {
        IRepository<Movie> movieIRepository = new InMemoryRepository<>();
        Movie movie = new Movie( "Inception", 2010, 20);
        movieIRepository.create(movie);
        movieIRepository.delete(movie.getId());
        assertEquals(0, movieIRepository.read().size());
    }

    @Test
    void UpdateMovie_should_updateThatMovieFromRepository() {
        IRepository<Movie> movieIRepository = new InMemoryRepository<>();
        Movie movie = new Movie( "Inception", 2010, 20);
        movieIRepository.create(movie);
        movie.setPrice(30);
        movie.setInTheatre(false);
        movieIRepository.update(movie);
        assertEquals(30,movieIRepository.read(movie.getId()).getPrice());
        assertEquals(false,movieIRepository.read(movie.getId()).isInTheatre());
        assertEquals(movie,movieIRepository.read(movie.getId()));
    }

}