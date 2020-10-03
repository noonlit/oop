package JavaFX.Service;

import JavaFX.Domain.Exceptions.InvalidReservationException;
import JavaFX.Domain.Movie;
import JavaFX.Domain.MovieReservation;
import JavaFX.Domain.Operations.AddOperation;
import JavaFX.Domain.Operations.AddRemoveReservations;
import JavaFX.Domain.ReservationPerCardClientViewModel;
import JavaFX.Domain.ReservationsPerMovieViewModel;
import JavaFX.Domain.Validators.MovieReservationValidator;
import JavaFX.Repository.IRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieReservationService {
    private IRepository<Movie> movieIRepository;
    private IRepository<MovieReservation> movieReservationIRepository;
    private UndoRedoService undoRedoService;

    private MovieReservationValidator movieReservationValidator;


    public MovieReservationService(IRepository<Movie> movieIRepository,
                                   IRepository<MovieReservation> movieReservationIRepository,
                                   MovieReservationValidator movieReservationValidator,
                                   UndoRedoService undoRedoService) {
        this.movieIRepository = movieIRepository;
        this.movieReservationIRepository = movieReservationIRepository;
        this.movieReservationValidator = movieReservationValidator;
        this.undoRedoService = undoRedoService;
    }


    public void addReservation(int reservedMovieId, int clientCardNumber, int hourOfStart, LocalDate reservationDate) {
        Movie reservedMovie = this.movieIRepository.read(reservedMovieId);
        if (reservedMovie == null) throw new InvalidReservationException("The movie ID does not exists!");
        MovieReservation movieReservation = new MovieReservation(reservedMovie, clientCardNumber, hourOfStart, reservationDate);
        this.movieReservationValidator.validate(movieReservation);
        this.movieReservationIRepository.create(movieReservation);
        this.undoRedoService.add(new AddOperation<MovieReservation>(movieReservationIRepository,movieReservation));
    }

    public List<MovieReservation> getAllReservations() {
        return this.movieReservationIRepository.read();
    }

    public List<ReservationsPerMovieViewModel> getAllSortedByNumberOfReservations() {
        List<ReservationsPerMovieViewModel> results = new ArrayList<>();

        getAllNumberOfReservations(results);

        results.sort(((o1, o2) -> {
            if (o1.getNumberOfReservation() < o2.getNumberOfReservation()) return 1;
            if (o1.getNumberOfReservation() > o2.getNumberOfReservation()) return -1;
            return 0;
        }));

        return results;

    }

    private void getAllNumberOfReservations(List<ReservationsPerMovieViewModel> results) {
        for (MovieReservation movieReservation : this.movieReservationIRepository.read()) {
            String movieName = movieReservation.getReservedMovie().getTitle();
            int numberOfReservation = reservationCounter(movieName);
            ReservationsPerMovieViewModel current = new ReservationsPerMovieViewModel(movieName, numberOfReservation);
            if (!results.contains(current)) {
                results.add(current);
            }
        }

    }

    private int reservationCounter(String movieName) {
        int counter = 0;
        for (MovieReservation movieReservation : this.movieReservationIRepository.read()) {
            if (movieReservation.getReservedMovie().getTitle().equals(movieName)) {
                counter++;
            }
        }
        return counter;
    }

    public List<ReservationPerCardClientViewModel> getCardNumbersSortedByNrReservations() {
        List<ReservationPerCardClientViewModel> results = new ArrayList<>();
        getAllReservationsPerCardNumber(results);

        results.sort(((o1, o2) -> {
            if (o1.getNumberOfReservation() < o1.getNumberOfReservation()) return 1;
            if (o1.getNumberOfReservation() > o2.getNumberOfReservation()) return -1;
            return 0;
        }));
        return results;
    }

    private void getAllReservationsPerCardNumber(List<ReservationPerCardClientViewModel> results) {
        for (MovieReservation movieReservation : this.movieReservationIRepository.read()) {
            int cardNumber = movieReservation.getClientCardNumber();
            int numberOfReservationsOfCard = reservationCardCounter(cardNumber);
            ReservationPerCardClientViewModel current = new ReservationPerCardClientViewModel(cardNumber,
                    numberOfReservationsOfCard);
            if (!results.contains(current)) {
                results.add(current);
            }

        }
    }

    private int reservationCardCounter(int cardNumber) {
        int counter = 0;
        for (MovieReservation movieReservation : this.movieReservationIRepository.read()) {
            if (movieReservation.getClientCardNumber() == cardNumber) {
                counter++;
            }
        }
        return counter;

    }

    public List<String> search(String text) {
        List<String> results = new ArrayList<>();
        searchMovie(text, results);
        searchReservations(text, results);
        return results;
    }

    private void searchReservations(String text, List<String> results) {
        for (MovieReservation movieReservation : this.movieReservationIRepository.read()) {
            if (movieReservation.toString().contains(text)) {
                results.add(movieReservation.toString());
            }
        }
    }

    private void searchMovie(String text, List<String> results) {
        for (Movie movie : this.movieIRepository.read()) {

            if (movie.toString().contains(text)) {
                results.add(movie.toString());
            }
        }
    }

    public List<MovieReservation> getReservationOfHourInterval(int start, int stop) {
        List<MovieReservation> results = new ArrayList<>();
        for (MovieReservation movieReservation : this.movieReservationIRepository.read()) {
            if (movieReservation.getHourOfStart() >= start && movieReservation.getHourOfStart() <= stop) {
                results.add(movieReservation);
            }
        }

        return results;
    }

    public void removeReservations(LocalDate start, LocalDate stop) {
        List<MovieReservation> movieReservations = new ArrayList<>();
        for (MovieReservation movieReservation : this.movieReservationIRepository.read()) {
            if(movieReservation.getReservationDate().isAfter(start)
                    && movieReservation.getReservationDate().isBefore(stop)){
                movieReservations.add(movieReservation);
                this.movieReservationIRepository.delete(movieReservation.getId());

            }
        }
        this.undoRedoService.add(new AddRemoveReservations(movieReservationIRepository,start,stop,movieReservations));
    }
}
