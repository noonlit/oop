package JavaFX;

import JavaFX.Domain.Movie;
import JavaFX.Domain.MovieReservation;
import JavaFX.Domain.Validators.MovieReservationValidator;
import JavaFX.Domain.Validators.MovieValidator;
import JavaFX.Repository.IRepository;
import JavaFX.Repository.InMemoryRepository;
import JavaFX.Service.MovieReservationService;
import JavaFX.Service.MovieService;
import JavaFX.Service.UndoRedoService;
import JavaFX.UserInterface.ConsoleCinema;

import java.time.LocalDate;

public class MainConsole {

    public static void main(String[] args) {
        IRepository<Movie> movieIRepository = new InMemoryRepository<>();
        IRepository<MovieReservation> movieReservationIRepository = new InMemoryRepository<>();
        MovieValidator movieValidator = new MovieValidator();
        MovieReservationValidator movieReservationValidator = new MovieReservationValidator();
        int contor=100;
        UndoRedoService undoRedoService= new UndoRedoService(movieIRepository,movieReservationIRepository);
        MovieService movieService = new MovieService(movieIRepository, movieValidator, undoRedoService);
        MovieReservationService movieReservationService = new MovieReservationService(movieIRepository,
                movieReservationIRepository,
                movieReservationValidator,
                undoRedoService);


        for (int i = 0; i < 10; i++) {
            movieService.addMovie( "movie " + i, 2000 + i, 20 + i);

        }

        for (int i = 1; i < 10; i++) {
            LocalDate localDate = LocalDate.of(2020, 1 + i, 12 + i);
            movieReservationService.addReservation( i, 40 * i, i + 2, localDate);
        }

        for (int i = 1; i < 6; i++) {
            contor++;
            LocalDate localDate = LocalDate.of(2020, 1 + i, 12 + i);
            movieReservationService.addReservation( i, 40 * i, i + 2, localDate);
        }
        for (int i = 1; i < 4; i++) {
            contor++;
            LocalDate localDate = LocalDate.of(2020, 1 + i, 12 + i);
            movieReservationService.addReservation( i, 40 * i, i + 2, localDate);
        }

        ConsoleCinema consoleCinema = new ConsoleCinema(movieReservationService, movieService,undoRedoService);

        consoleCinema.runUserInterface();
    }

}
