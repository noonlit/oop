package Cinema.Service;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Booking;
import Cinema.Domain.Customer;
import Cinema.Domain.Movie;
import Cinema.Domain.Validator.MovieValidator;
import Cinema.Exception.EntityNotFoundException;
import Cinema.Exception.LogicException;
import Cinema.Repository.BookingRepository;
import Cinema.Repository.History.History;
import Cinema.Repository.MovieRepository;
import Cinema.Repository.RepositoryManager;
import Cinema.Repository.Storage.JsonStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {
    MovieService movieService;
    RepositoryManager repositoryManager;

    @BeforeEach
    void setUp() {
        MovieRepository movieRepository = new MovieRepository(new History(), new History(), new JsonStorage("db_movie.json", Movie.class));
        BookingRepository bookingRepository = new BookingRepository(new History(), new History(), new JsonStorage("db_booking.json", Booking.class));

        repositoryManager = new RepositoryManager();
        repositoryManager.addRepository(bookingRepository);
        repositoryManager.addRepository(movieRepository);

        MovieValidator validator = new MovieValidator();
        movieService = new MovieService(repositoryManager, validator);
    }

    @Test
    void createNewMovieShouldAddMovieToRepository() {
        Movie movie = movieService.createMovie("Corpse Bride", 2005, 10, true);

        assertNotNull(movie.getId());
    }

    @Test
    void updateExistingMovieDataShouldUpdateMovieData() {
        Movie movie = movieService.createMovie("CB", 2004, 10, false);
        movieService.updateMovie(movie.getId(), "Corpse Bride", 2005, 100, true);
        Movie updatedMovie = (Movie) movieService.findOne("Corpse Bride");

        assertEquals(2005, updatedMovie.getYear());
        assertEquals(100, updatedMovie.getPrice());
        assertTrue(updatedMovie.isRunning());
    }

    @Test
    void updateNonExistingMovieShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> movieService.updateMovie(
                        10,
                        "Corpse Bride",
                        2005,
                        100,
                        true
                )
        );
    }

    @Test
    void deleteExistingMovieShouldReturnTrue() {
        Movie movie = movieService.createMovie("Corpse Bride", 2005, 10, true);
        assertTrue(movieService.delete(movie.getId()));
    }

    @Test
    void findMovieByTitleShouldMatchOne() {
        movieService.createMovie("Corpse Bride", 2005, 10, true);
        movieService.createMovie("Doctor Sleep", 2020, 10, true);
        List<AbstractEntity> movieList = movieService.find("corp");
        assertEquals(1, movieList.size());
    }

    @Test
    void findMovieByNullShouldMatchTwo() {
        movieService.createMovie("Corpse Bride", 2005, 10, true);
        movieService.createMovie("Doctor Sleep", 2020, 10, true);
        List<AbstractEntity> movieList = movieService.find(null);
        assertEquals(2, movieList.size());
    }


    @Test
    void findMovieByYearShouldMatchOne() {
        movieService.createMovie("Corpse Bride", 2005, 10, true);
        movieService.createMovie("Doctor Sleep", 2020, 10, true);
        List<AbstractEntity> movieList = movieService.find("2005");
        assertEquals(1, movieList.size());
    }

    @Test
    void findMoviesByTitleShouldMatchTwo() {
        movieService.createMovie("Corpse Bride", 2005, 10, true);
        movieService.createMovie("Doctor Sleep", 2020, 10, true);
        List<AbstractEntity> movieList = movieService.findAll();
        assertEquals(2, movieList.size());
    }

    @Test
    void undoDeleteShouldRevertDelete() {
        Movie movie = movieService.createMovie("Corpse Bride", 2005, 10, true);
        movieService.delete(movie.getId());
        movieService.undo();
        List<AbstractEntity> movieList = movieService.find("corp");
        assertEquals(1, movieList.size());
    }

    @Test
    void undoDeleteShouldRevertDeletedBookingsToo() {
        // create movie and associated bookings
        Movie movie = movieService.createMovie("Corpse Bride", 2005, 10, true);
        LocalDateTime time = LocalDateTime.now().plusHours(5);

        BookingRepository bookingRepository = (BookingRepository) repositoryManager.getRepository(BookingRepository.class);
        bookingRepository.save(new Booking(movie, new Customer(1234567), time));
        bookingRepository.save(new Booking(movie, new Customer(1224567), time.plusHours(1)));
        bookingRepository.save(new Booking(movie, new Customer(1244567), time.plusHours(2)));

        // delete movie and check that bookings were deleted too
        movieService.delete(movie.getId());
        List<AbstractEntity> movieList = movieService.find("corp");
        assertEquals(0, movieList.size());
        List<AbstractEntity> bookingList = movieService.findBookingsByMovieId(movie.getId());
        assertEquals(0, bookingList.size());

        // undo deletion
        movieService.undo();

        // check that both movie and bookings were restored
        movieList = movieService.find("corp");
        assertEquals(1, movieList.size());
        bookingList = movieService.findBookingsByMovieId(movie.getId());
        assertEquals(3, bookingList.size());
    }

    @Test
    void redoDeleteAfterUndoShouldDelete() {
        Movie movie = movieService.createMovie("Corpse Bride", 2005, 10, true);
        movieService.delete(movie.getId());
        movieService.undo();
        movieService.redo();
        List<AbstractEntity> movieList = movieService.find("corp");
        assertEquals(0, movieList.size());
    }

    @Test
    void redoDeleteAfterUndoShouldAlsoDeleteBookings() {
        // create movie and corresponding bookings
        Movie movie = movieService.createMovie("Corpse Bride", 2005, 10, true);
        LocalDateTime time = LocalDateTime.now().plusHours(5);

        BookingRepository bookingRepository = (BookingRepository) repositoryManager.getRepository(BookingRepository.class);
        bookingRepository.save(new Booking(movie, new Customer(1234567), time));
        bookingRepository.save(new Booking(movie, new Customer(1234568), time.plusHours(1)));

        movieService.delete(movie.getId());
        movieService.undo();
        movieService.redo();

        List<AbstractEntity> movieList = movieService.find("corp");
        assertEquals(0, movieList.size());
        List<AbstractEntity> bookingList = movieService.findBookingsByMovieId(movie.getId());
        assertEquals(0, bookingList.size());
    }

    @Test
    void updateMoviesPriceShouldUpdateMoviesUnderThreshold() {
        movieService.createMovie("Mad Max", 2015, 100, true);
        movieService.createMovie("Doctor Sleep", 2019, 10, true);
        movieService.createMovie("Corpse Bride", 2005, 10, true);

        int updated = movieService.updateMoviesPrice(11, 200);
        assertEquals(2, updated);
    }

    @Test
    void updateMoviesPriceWithZeroValueShouldThrowException() {
        movieService.createMovie("Doctor Sleep", 2019, 10, true);
        assertThrows(LogicException.class,
                () -> movieService.updateMoviesPrice(11, -1)
        );
    }
}