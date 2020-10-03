package Cinema.Domain;

import Cinema.Util.DateConverter;
import java.time.LocalDateTime;

/**
 * Represents a booking in The cinema.
 */
public class Booking extends AbstractEntity {
    private Movie movie;
    private Customer customer;
    private LocalDateTime dateTime;

    /**
     * @param id The booking ID
     * @param movie The movie this booking is associated with.
     * @param customer The customer making the booking.
     * @param dateTime The date and time for the booking.
     */
    public Booking(Integer id, Movie movie, Customer customer, LocalDateTime dateTime) {
        this.id = id;
        this.movie = movie;
        this.customer = customer;
        this.dateTime = dateTime;
    }

    /**
     * @param movie The movie this booking is associated with.
     * @param customer The customer making the booking.
     * @param dateTime The date and time for the booking.
     */
    public Booking(Movie movie, Customer customer, LocalDateTime dateTime) {
        this.movie = movie;
        this.customer = customer;
        this.dateTime = dateTime;
    }

    /**
     * @param dateTime The date and time for the booking.
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @param customer The customer who made the booking
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return The day of the booking
     */
    public int getDay() {
        return dateTime.getDayOfMonth();
    }

    /**
     * @return The month of the booking
     */
    public int getMonth() {
        return dateTime.getMonth().getValue();
    }

    /**
     * @return The year of the booking
     */
    public int getYear() {
        return dateTime.getYear();
    }

    /**
     * @return The date and time of the booking as a local date time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * @return The minute of the booking
     */
    public int getMinute() {
        return dateTime.getMinute();
    }

    /**
     * @return The hour of the booking
     */
    public int getHour() {
        return dateTime.getHour();
    }

    /**
     * @return A string representation of the date for the booking
     */
    public String getDateTimeString() {
        return DateConverter.dateTimeToString(dateTime);
    }

    /**
     * @return The movie corresponding to the booking
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * @param movie The movie corresponding to the booking
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * @return The title of the movie corresponding to the booking
     */
    public String getMovieTitle() {
        return movie.getTitle();
    }

    /**
     * @return The customer corresponding to the booking
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @return The customer card number
     */
    public int getCustomerCardNumber() {
        return customer.getCardNumber();
    }

    /**
     * @return A copy of the current instance
     */
    public Booking clone() {
        return new Booking(getId(), getMovie(), getCustomer(), getDateTime());
    }

    /**
     * @return A string representation of the current instance
     */
    public String toString() {
        return String.format("Booking with ID %d, for movie %s, for customer with card %s, at date and time %s.", getId(), getMovieTitle(), getCustomerCardNumber(), getDateTimeString());
    }
}
