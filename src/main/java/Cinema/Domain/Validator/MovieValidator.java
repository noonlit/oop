package Cinema.Domain.Validator;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Movie;
import Cinema.Exception.LogicException;

public class MovieValidator implements ValidatorInterface {
    /**
     * Validates a movie entry.
     *
     * A movie entry must have a positive price.
     *
     * @param entity The movie entry.
     */
    public void validate(AbstractEntity entity) {
        Movie movie = (Movie) entity;
        int minMoviePrice = getMinValidMoviePrice();
        if (movie.getPrice() < minMoviePrice) {
            throw new LogicException("The price is not valid! Please insert a value greater than or equal to " + minMoviePrice);
        }
    }

    /**
     * @return The minimum valid price for a movie.
     */
    public int getMinValidMoviePrice() {
        return 1;
    }
}
