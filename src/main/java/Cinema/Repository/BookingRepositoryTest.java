package Cinema.Repository;

import Cinema.Domain.Booking;
import Cinema.Domain.Customer;
import Cinema.Domain.Movie;
import Cinema.Repository.History.History;
import Cinema.Repository.Storage.MemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingRepositoryTest {

    private BookingRepository bookingRepository;
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        this.bookingRepository = new BookingRepository(new History(), new History(), new MemoryStorage());
        this.movieRepository = new MovieRepository(new History(), new History(), new MemoryStorage());
    }

    @Test
    void findBookingShouldFindExistingBooking() {
        Movie movie = new Movie(1, "Corpse Bride", 2005, 10, true);
        movieRepository.save(movie);
        assertTrue(movieRepository.entryExists(1));
        LocalDateTime time = LocalDateTime.now().plusHours(5);
        bookingRepository.save(new Booking(1, movie, new Customer(1234567), time));

        assertEquals(1, bookingRepository.find("1234567").size());
    }
}