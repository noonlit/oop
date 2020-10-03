package Cinema.Service;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Movie;
import Cinema.Domain.Validator.MovieValidator;
import Cinema.Domain.Validator.ValidatorInterface;
import Cinema.Exception.EntityNotFoundException;
import Cinema.Exception.LogicException;
import Cinema.Repository.BookingRepository;
import Cinema.Repository.MovieRepository;
import Cinema.Repository.RepositoryManager;

import java.util.List;

public class MovieService extends AbstractService {
    public MovieService(RepositoryManager repositoryManager, ValidatorInterface validator) {
        super(repositoryManager, validator);
    }

    @Override
    protected void setDefaultRepository() {
        this.defaultRepository = repositoryManager.getRepository(MovieRepository.class);
    }

    /**
     * Creates a new movie with the given characteristics.
     *
     * @param title movie title
     * @param year movie year
     * @param price movie price
     * @param isRunning whether The movie is running
     * @return The updated movie
     */
    public Movie createMovie(String title, int year, int price, boolean isRunning) {
        Movie movie = new Movie(title, year, price, isRunning);
        validator.validate(movie);
        return (Movie) persist(movie);
    }

    /**
     * Updates the movie with the given id.
     *
     * @param id movie id
     * @param title movie title
     * @param year movie year
     * @param price movie price
     * @param isRunning whether The movie is running
     * @return The updated movie
     */
    public Movie updateMovie(int id, String title, int year, int price, boolean isRunning) {
        if (!defaultRepository.entryExists(id)) {
            throw new EntityNotFoundException("Movie doesn't exist!");
        }

        Movie movie = (Movie) defaultRepository.get(id);
        movie.setTitle(title);
        movie.setYear(year);
        movie.setPrice(price);
        movie.setIsRunning(isRunning);

        validator.validate(movie);

        return (Movie) persist(movie);
    }

    /**
     * Updates movies with a price <= minOldPrice, setting their price to newPrice.
     *
     * @param minOldPrice The min price threshold.
     * @param newPrice The new value for the price.
     * @return Number of affected movies.
     */
    public int updateMoviesPrice(int minOldPrice, int newPrice) {
        MovieValidator movieValidator = (MovieValidator) validator;
        int minMoviePrice = movieValidator.getMinValidMoviePrice();
        if (newPrice < minMoviePrice) {
            throw new LogicException("The price is not valid! Please insert a value greater than or equal to " + minMoviePrice);
        }

        MovieRepository movieRepository = (MovieRepository) defaultRepository;
        return movieRepository.updatePrices(minOldPrice, newPrice);
    }

    /**
     * Returns all the bookings for the movie with the given ID.
     *
     * @param id The movie ID.
     * @return The associated bookings.
     */
    public List<AbstractEntity> findBookingsByMovieId(int id) {
        BookingRepository repository = (BookingRepository) repositoryManager.getRepository(BookingRepository.class);
        return repository.findByMovieId(id);
    }

    /**
     * Deletes the movie with the given ID.
     * Also clears the bookings associated with that movie.
     */
    public boolean delete(int id) {
        MovieRepository repository = (MovieRepository) defaultRepository;
        return repository.deleteAndClearBookings(id, getBookingRepository());
    }

    /**
     * @return The booking repository.
     */
    private BookingRepository getBookingRepository() {
        return (BookingRepository) repositoryManager.getRepository(BookingRepository.class);
    }
}
