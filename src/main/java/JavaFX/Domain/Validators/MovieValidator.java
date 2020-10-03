package JavaFX.Domain.Validators;

import JavaFX.Domain.Exceptions.InvalidMovieException;
import JavaFX.Domain.Movie;

public class MovieValidator {

    public void validate(Movie movie){
        String messages="";
        if(movie.getPrice() < 0) {
            messages += " the price must be pozitive! ";
        }

        if(movie.getYearOfRelease() < 1850){
            messages += " impossible year of release! ";
        }

        if(!messages.equals("")) {
            throw new InvalidMovieException(messages);
        }
    }
}
