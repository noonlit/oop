package JavaFX.Domain.Validators;

import JavaFX.Domain.Exceptions.InvalidReservationException;
import JavaFX.Domain.MovieReservation;

public class MovieReservationValidator {

    public void validate(MovieReservation movieReservation){
        String messages="";
        if(!movieReservation.getReservedMovie().isInTheatre()){
            messages += "The movie is out of Theatre! ";
        }

        if(movieReservation.getHourOfStart()<0 || movieReservation.getHourOfStart()>23){
            messages += "wrong hour! ";
        }

        if(!messages.equals("")){
            throw new InvalidReservationException(messages);
        }
    }
}
