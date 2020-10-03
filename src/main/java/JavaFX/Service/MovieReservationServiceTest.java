package JavaFX.Service;

import JavaFX.Domain.*;
import JavaFX.Domain.Exceptions.InvalidReservationException;
import JavaFX.Domain.Validators.MovieReservationValidator;
import JavaFX.Domain.Validators.MovieValidator;
import JavaFX.Repository.IRepository;
import JavaFX.Repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieReservationServiceTest {
    IRepository<Movie> movieIRepository = new InMemoryRepository<>();
    IRepository<MovieReservation> movieReservationIRepository = new InMemoryRepository<>();
    MovieValidator movieValidator = new MovieValidator();
    MovieReservationValidator movieReservationValidator = new MovieReservationValidator();
    UndoRedoService undoRedoService = new UndoRedoService(movieIRepository, movieReservationIRepository);
    MovieService movieService = new MovieService(movieIRepository, movieValidator, undoRedoService);
    MovieReservationService movieReservationService = new MovieReservationService(movieIRepository,
            movieReservationIRepository,
            movieReservationValidator,
            undoRedoService);
    Movie movie = new Movie("Inception", 2010, 20);
    LocalDate localDate = LocalDate.of(2020, 01, 01);
    LocalDate localDate2 = LocalDate.of(2020, 07, 30);
    LocalDate localDate3 = LocalDate.of(2021, 07, 30);
    Movie movie2 = new Movie("Dracula", 2009, 20);
    MovieReservation movieReservation = new MovieReservation(movie2, 1234, 9, localDate);
    MovieReservation movieReservation2 = new MovieReservation(movie2, 1234, 11, localDate2);
    MovieReservation movieReservation3 = new MovieReservation(movie2, 1234, 12, localDate2);
    MovieReservation movieReservation4 = new MovieReservation(movie, 4567, 21, localDate3);

    @BeforeEach
    void populate() {
        movieService.addMovie(movie.getTitle(),
                movie.getYearOfRelease(),
                movie.getPrice());
        movieService.addMovie(movie2.getTitle(),
                movie2.getYearOfRelease(),
                movie2.getPrice());
        movieReservationService.addReservation(1,
                movieReservation.getClientCardNumber(),
                movieReservation.getHourOfStart(),
                movieReservation.getReservationDate());
        movieReservationService.addReservation(
                1,
                movieReservation2.getClientCardNumber(),
                movieReservation2.getHourOfStart(),
                movieReservation2.getReservationDate());
        movieReservationService.addReservation(
                1,
                movieReservation3.getClientCardNumber(),
                movieReservation3.getHourOfStart(),
                movieReservation3.getReservationDate());
        movieReservationService.addReservation(
                0,
                movieReservation4.getClientCardNumber(),
                movieReservation4.getHourOfStart(),
                movieReservation4.getReservationDate());
    }

    @Test
    void addingAValidReservation_should_saveThatReservationToRepository() {
        assertEquals(4, movieReservationIRepository.read().size());
        assertEquals(movieReservation, movieReservationIRepository.read(movieReservation.getId()));
        undoRedoService.undo();
        assertEquals(3, movieReservationIRepository.read().size());
        undoRedoService.redo();
        assertEquals(4, movieReservationIRepository.read().size());
    }

    @Test
    void addingWrongIdMovieToReservation_should_raiseInvalidReservationException() {
        assertThrows(InvalidReservationException.class, () -> {
                    movieReservationService.addReservation(999,
                            movieReservation.getClientCardNumber(),
                            movieReservation.getHourOfStart(),
                            movieReservation.getReservationDate());
                }
        );
    }

    @Test
    void sortingMoviesByNumberOfReservations_should_returnASortedListOfReservationsPerMoviesViewModel() {

        List<ReservationsPerMovieViewModel> test = new ArrayList<>();
        test.add(new ReservationsPerMovieViewModel(movie2.getTitle(), 3));
        test.add(new ReservationsPerMovieViewModel(movie.getTitle(), 1));
        assertEquals(test, movieReservationService.getAllSortedByNumberOfReservations());

    }

    @Test
    void sortingCardNumbersByNumberOfReservations_should_returnASortedListOfReservationsPerCardClientViewModel() {
        List<ReservationPerCardClientViewModel> test = new ArrayList<>();
        test.add(new ReservationPerCardClientViewModel(1234, 3));
        test.add(new ReservationPerCardClientViewModel(4567, 1));
        assertEquals(test, movieReservationService.getCardNumbersSortedByNrReservations());
    }

    @Test
    void searchingByAText_should_returnAListOfTheSearchedResults() {
        String text = "Inception";
        List<String> test = new ArrayList<>();
        test.add(movie.toString());
        test.add(movieReservationIRepository.read(3).toString());
        assertEquals(test, movieReservationService.search(text));
    }

    @Test
    void searchReservationsByHourInterval_should_returnACorrectListOfMovieReservations() {
        int start = 10;
        int stop = 13;
        List<MovieReservation> test = new ArrayList<>();
        test.add(movieReservationIRepository.read(1));
        test.add(movieReservationIRepository.read(2));
        assertEquals(test, movieReservationService.getReservationOfHourInterval(start, stop));

    }

    @Test
    void RemoveReservationsByDateInterval_should_RemoveTheReservationsFromRepository(){
        LocalDate start = LocalDate.of(2020,02,1);
        LocalDate stop = LocalDate.of(2021,01,01);
        List<MovieReservation> test = new ArrayList<>();
        test.add(movieReservationIRepository.read(0));
        test.add(movieReservationIRepository.read(3));
        movieReservationService.removeReservations(start,stop);
        assertEquals(2,movieReservationIRepository.read().size());
        assertEquals(test,movieReservationIRepository.read());
        undoRedoService.undo();
        assertEquals(4,movieReservationIRepository.read().size());
        assertNotEquals(test,movieReservationIRepository.read());
        undoRedoService.redo();
        assertEquals(2,movieReservationIRepository.read().size());
        assertEquals(test,movieReservationIRepository.read());

    }
}