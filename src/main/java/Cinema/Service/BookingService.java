package Cinema.Service;

import Cinema.Domain.Booking;
import Cinema.Domain.Customer;
import Cinema.Domain.Movie;
import Cinema.Domain.Validator.ValidatorInterface;
import Cinema.Exception.EntityNotFoundException;
import Cinema.Exception.FormatException;
import Cinema.Repository.BookingRepository;
import Cinema.Repository.MovieRepository;
import Cinema.Repository.RepositoryManager;
import Cinema.Util.DateConverter;
import Cinema.ViewModel.CustomerCardNumberAndBookingCount;
import Cinema.ViewModel.MovieAndBookingCount;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService extends AbstractService {
    public BookingService(RepositoryManager repositoryManager, ValidatorInterface validator) {
        super(repositoryManager, validator);
    }

    @Override
    protected void setDefaultRepository() {
        defaultRepository = repositoryManager.getRepository(BookingRepository.class);
    }

    /**
     * Creates a new booking for the movie with the given title.
     *
     * @param movieTitle The movie title.
     * @param cardNr The customer's card number.
     * @param dateTime The date and time for the booking.
     * @return The created booking.
     */
    public Booking createBooking(String movieTitle, int cardNr, String dateTime) {
        Movie movie = (Movie) getMovieRepository().findOne(movieTitle);
        LocalDateTime fullDateTime = DateConverter.toDateTimeFromString(dateTime);
        Customer customer = new Customer(cardNr);
        Booking booking = new Booking(movie, customer, fullDateTime);

        validator.validate(booking);
        return (Booking) persist(booking);
    }

    public Booking updateBooking(int id, String movieTitle, int cardNr, String dateTime) {
        if (!defaultRepository.entryExists(id)) {
            throw new EntityNotFoundException("The booking doesn't exist!");
        }

        Booking booking = (Booking) defaultRepository.get(id);
        Movie movie = (Movie) getMovieRepository().findOne(movieTitle);

        booking.setMovie(movie);

        try {
            LocalDateTime fullDateTime = DateConverter.toDateTimeFromString(dateTime);
            booking.setDateTime(fullDateTime);
        } catch (Exception e) {
            throw new FormatException("Please enter a correctly formatted date and time, e.g. " + DateConverter.dateTimeFormatExample);
        }

        Customer customer = new Customer(cardNr);
        booking.setCustomer(customer);

        validator.validate(booking);
        return (Booking) persist(booking);
    }

    /**
     * Retrieves a list of bookings between the given start and end (expected as HH:mm strings).
     *
     * @param start The start time.
     * @param end The end time.
     * @return A List of bookings.
     */
    public List<Booking> find(String start, String end) {
        try {
            LocalDateTime startDateTime = DateConverter.toTimeFromString(start);
            LocalDateTime endDateTime = DateConverter.toTimeFromString(end);

            // The defaultRepository associated with this service is a booking defaultRepository.
            BookingRepository bookingRepository = (BookingRepository) defaultRepository;
            return bookingRepository.find(startDateTime, endDateTime);
        } catch (ParseException e) {
            throw new FormatException("Please enter a correctly formatted time, e.g. " + DateConverter.timeFormatExample);
        }
    }

    /**
     * Deletes bookings between the given start and the end datetimes.
     * The start and end datetimes are expected as something like: "15/04/2020 at 01:00"
     *
     * @param start The start date
     * @param end The end date
     * @return number of deleted rows
     */
    public int delete(String start, String end) {
            LocalDateTime startDateTime = DateConverter.toDateTimeFromString(start);
            LocalDateTime endDateTime = DateConverter.toDateTimeFromString(end);

            // The defaultRepository associated with this service is a booking repository.
            BookingRepository bookingRepository = (BookingRepository) defaultRepository;
            return bookingRepository.delete(startDateTime, endDateTime);
    }

    /**
     * Returns a collection of view models that contain the customer card number and the corresponding number of bookings,
     * sorted by the bookings count.
     *
     * @return The view models
     */
    public List<CustomerCardNumberAndBookingCount> getCustomerCardNumbersSortedByBookingsCount() {
        BookingRepository bookingRepository = (BookingRepository) defaultRepository;
        List<Booking> bookings = bookingRepository.getBookingsSortedByCustomerCreditCardNumber();

        // build list of view models from the sorted bookings
        ArrayList<CustomerCardNumberAndBookingCount> viewModels = new ArrayList<>();
        int currentCardNumber = 0;
        int currentBookings  = 0;
        for (Booking booking : bookings) {
            int customerCardNumber = booking.getCustomerCardNumber();
            if (currentCardNumber == 0) {
                currentCardNumber = customerCardNumber;
            }

            if (currentCardNumber != customerCardNumber) {
                viewModels.add(new CustomerCardNumberAndBookingCount(currentCardNumber, currentBookings));
                currentCardNumber = customerCardNumber;
                currentBookings = 1;
                continue;
            }

            currentBookings++;
        }

        viewModels.add(new CustomerCardNumberAndBookingCount(currentCardNumber, currentBookings));

        // sort by number of bookings
        viewModels.sort((c1, c2) -> c2.bookings - c1.bookings);

        return viewModels;
    }

    /**
     * Returns a collection of view models that contain movies and their corresponding booking counts,
     * sorted by the bookings count.
     *
     * @return The view models
     */
    public List<MovieAndBookingCount> getMoviesSortedByBookingsCount() {
        BookingRepository bookingRepository = (BookingRepository) defaultRepository;
        List<Booking> bookings = bookingRepository.getBookingsSortedByMovieId();

        // build list of view models from the sorted bookings
        ArrayList<MovieAndBookingCount> viewModels = new ArrayList<>();
        Movie currentMovie = null;
        int currentBookings  = 0;
        for (Booking booking : bookings) {
            Movie movie = booking.getMovie();
            if (currentMovie == null) {
                currentMovie = movie;
            }
            if (!currentMovie.getId().equals(movie.getId())) {
                viewModels.add(new MovieAndBookingCount(currentMovie, currentBookings));
                currentMovie = movie;
                currentBookings = 1;
                continue;
            }

            currentBookings++;
        }

        viewModels.add(new MovieAndBookingCount(currentMovie, currentBookings));

        // sort by number of bookings
        viewModels.sort((c1, c2) -> c2.bookings - c1.bookings);

        return viewModels;
    }

    private MovieRepository getMovieRepository()
    {
        return (MovieRepository) repositoryManager.getRepository(MovieRepository.class);
    }
}
