package Cinema.ViewModel;
import Cinema.Domain.Movie;

/**
 * View model for displaying movies + corresponding bookings count.
 */
public class MovieAndBookingCount {
    public Movie movie;
    public int bookings;

    public MovieAndBookingCount(Movie movie, int bookings) {
        this.movie = movie;
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return String.format("Movie: %s. The movie has %d bookings.", movie, bookings);
    }
}
