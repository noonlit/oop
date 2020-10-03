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
import JavaFX.UserInterface.AppCinema;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cinema/app.fxml"));
        Parent root = fxmlLoader.load();

        AppCinema controller =  fxmlLoader.getController();

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

        controller.setServices(movieReservationService, movieService, undoRedoService);

        primaryStage.setTitle("Cinema Manager");
        primaryStage.setScene(new Scene(root, 650, 500));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
