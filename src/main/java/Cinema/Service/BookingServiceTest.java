package Cinema.Service;

import Cinema.Domain.Booking;
import Cinema.Domain.Movie;
import Cinema.Domain.Validator.BookingValidator;
import Cinema.Exception.EntityNotFoundException;
import Cinema.Exception.FormatException;
import Cinema.Repository.BookingRepository;
import Cinema.Repository.History.History;
import Cinema.Repository.MovieRepository;
import Cinema.Repository.RepositoryManager;
import Cinema.Repository.Storage.JsonStorage;
import Cinema.ViewModel.CustomerCardNumberAndBookingCount;
import Cinema.ViewModel.MovieAndBookingCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    BookingService bookingService;
    RepositoryManager repositoryManager;

    @BeforeEach
    void setUp() {
        MovieRepository movieRepository = new MovieRepository(new History(), new History(), new JsonStorage("db_movie.json", Movie.class));
        BookingRepository bookingRepository = new BookingRepository(new History(), new History(), new JsonStorage("db_booking.json", Booking.class));
        BookingValidator validator = new BookingValidator();

        repositoryManager = new RepositoryManager();
        repositoryManager.addRepository(bookingRepository);
        repositoryManager.addRepository(movieRepository);

        bookingService = new BookingService(repositoryManager, validator);
    }

    @Test
    void createNewBookingShouldAddBookingToRepository() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        Booking booking = bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 18:00");

        assertNotNull(booking.getId());
    }

    @Test
    void createNewBookingShouldFailToAddBookingToRepository() {
        assertThrows(EntityNotFoundException.class,
                () -> bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 18:00")
        );
    }

    @Test
    void updateBookingShouldFailWithNonExistingEntityException() {
        assertThrows(EntityNotFoundException.class,
                () -> bookingService.updateBooking(10000, "Corpse Bride", 1234567, "15/04/2020 at 18:00")
        );
    }

    @Test
    void updateBookingShouldFailWithFormatException() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        bookingService.createBooking( "Corpse Bride", 1234567, "15/04/2020 at 18:00");

        assertThrows(FormatException.class,
                () -> bookingService.updateBooking(1, "Corpse Bride", 1234567, "abcd")
        );
    }

    @Test
    void updateExistingBookingDataShouldUpdateBookingData() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        bookingService.createBooking( "Corpse Bride", 1234567, "15/04/2020 at 18:00");

        Booking booking = (Booking) bookingService.findOne("1234567");
        bookingService.updateBooking(booking.getId(), "Corpse Bride", 7654321, "15/04/2020 at 18:00");
        Booking updatedBooking = (Booking) bookingService.findOne("7654321");

        assertNotNull(updatedBooking);
    }

    @Test
    void findBookingsShouldMatchTwo() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 18:00");
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 20:00");

        List<Booking> bookingList = bookingService.find("17:00", "21:00");

        assertEquals(2, bookingList.size());
    }
    @Test
    void findBookingsShouldMatchOne() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 18:00");
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 20:00");

        List<Booking> bookingList = bookingService.find("18:10", "21:00");

        assertEquals(1, bookingList.size());
    }

    @Test
    void findBookingsShouldThrowParseError() {
        assertThrows(FormatException.class,
                () -> bookingService.find("1810", "2110")
        );
    }

    @Test
    void deleteBookingsShouldDeleteTwo() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 18:00");
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 20:00");

        int deleted = bookingService.delete("15/04/2020 at 01:00", "15/04/2020 at 23:00");

        assertEquals(2, deleted);
    }

    @Test
    void getCustomerCardNumbersSortedByBookingsCountShouldGetSortedElements() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));

        // two bookings with the same card
        bookingService.createBooking("Corpse Bride", 1357910, "12/04/2020 at 20:00");
        bookingService.createBooking("Corpse Bride", 1357910, "13/04/2020 at 20:00");

        // three bookings with the same card
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 18:00");
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 20:00");
        bookingService.createBooking("Corpse Bride", 1234567, "15/06/2020 at 20:00");

        // one booking
        bookingService.createBooking("Corpse Bride", 7654321, "11/04/2020 at 20:00");

        List<CustomerCardNumberAndBookingCount> cardNumbers = bookingService.getCustomerCardNumbersSortedByBookingsCount();

        assertEquals(cardNumbers.get(0).cardNumber, 1234567);
        assertEquals(cardNumbers.get(1).cardNumber, 1357910);
        assertEquals(cardNumbers.get(2).cardNumber, 7654321);

        assertEquals(cardNumbers.get(0).bookings, 3);
        assertEquals(cardNumbers.get(1).bookings, 2);
        assertEquals(cardNumbers.get(2).bookings, 1);
    }

    @Test
    void getMoviesSortedByBookingsCountShouldGetSortedElements() {
        MovieRepository movieRepository = (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
        movieRepository.save(new Movie(1,"Corpse Bride", 2005, 10, true));
        movieRepository.save(new Movie(2, "Mad Max", 2015, 10, true));
        movieRepository.save(new Movie(3, "Doctor Sleep", 2019, 10, true));

        bookingService.createBooking("Mad Max", 1357910, "12/04/2020 at 20:00");
        bookingService.createBooking("Mad Max", 1357910, "13/04/2020 at 20:00");

        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 18:00");
        bookingService.createBooking("Corpse Bride", 1234567, "15/04/2020 at 20:00");
        bookingService.createBooking("Corpse Bride", 1234567, "15/06/2020 at 20:00");

        bookingService.createBooking("Doctor Sleep", 7654321, "11/04/2020 at 20:00");

        List<MovieAndBookingCount> movies = bookingService.getMoviesSortedByBookingsCount();

        assertEquals(movies.get(0).movie.getId(), 1);
        assertEquals(movies.get(1).movie.getId(), 2);
        assertEquals(movies.get(2).movie.getId(), 3);

        assertEquals(movies.get(0).bookings, 3);
        assertEquals(movies.get(1).bookings, 2);
        assertEquals(movies.get(2).bookings, 1);
    }
}