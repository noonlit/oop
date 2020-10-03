package JavaFX.UserInterface;

import JavaFX.Domain.*;
import JavaFX.Domain.Exceptions.InvalidMovieException;
import JavaFX.Domain.Exceptions.InvalidReservationException;
import JavaFX.Domain.Exceptions.WrongIdException;
import JavaFX.Service.MovieReservationService;
import JavaFX.Service.MovieService;
import JavaFX.Service.UndoRedoService;


import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleCinema implements IUserInterface {
    private MovieReservationService movieReservationService;
    private MovieService movieService;
    private UndoRedoService undoRedoService;

    public ConsoleCinema(MovieReservationService movieReservationService, MovieService movieService,
                         UndoRedoService undoRedoService) {
        this.movieReservationService = movieReservationService;
        this.movieService = movieService;
        this.undoRedoService = undoRedoService;
    }

    private String readString(String prompter) {
        System.out.print(prompter);
        Scanner scan = new Scanner(System.in);
        String sir = scan.nextLine();
        return sir;
    }

    public void runUserInterface() {
        char option = showMenu();
        while (option != 'x') {
            switch (option) {
                case '1':
                    this.handleAddMovie();
                    break;

                case '2':
                    this.handleAddReservation();
                    break;
                case '3':
                    this.handleShowAllMovies();
                    break;
                case '4':
                    this.handleShowAllReservations();
                    break;
                case '5':
                    this.handleSortMoviesByNumberOfReservations();
                    break;
                case '6':
                    this.handleSortClientCardNumbersByNumberOfReservations();
                    break;
                case '7':
                    this.handleSearch();
                    break;
                case '8':
                    this.handleHourInterval();
                    break;
                case '9':
                    this.handleIncreasePrice();
                    break;
                case 'a':
                    this.handleDecreasePrice();
                    break;
                case 'b':
                    this.handleRemoveReservations();
                    break;
                case 'q':
                    this.handleUndo();
                    break;
                case 'w':
                    this.handleRedo();
                     break;
                default:
                    System.out.println(" Invalid option");

            }
            option = showMenu();
        }
        System.out.println("Program closed");

    }

    private void handleRedo() {
        if(this.undoRedoService.redo()){
            System.out.println("redo done");
        }else{
            System.out.println("no more redo!");
        }
    }

    private void handleUndo() {
        if(this.undoRedoService.undo()){
            System.out.println("undo done");
        }else{
            System.out.println("no more undo!");
        }
    }

    private void handleRemoveReservations() {
        try {
            System.out.println("from:");
            int startYear = Integer.parseInt(readString("year: "));
            int startMonth = Integer.parseInt(readString("month: "));
            int startDay = Integer.parseInt(readString("day: "));
            System.out.println("to: ");
            int stopYear = Integer.parseInt(readString("year: "));
            int stopMonth = Integer.parseInt(readString("month: "));
            int stopDay = Integer.parseInt(readString("day: "));
            LocalDate start = LocalDate.of(startYear, startMonth, startDay);
            LocalDate stop = LocalDate.of(stopYear, stopMonth, stopDay);
            this.movieReservationService.removeReservations(start, stop);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void handleDecreasePrice() {
        try {
            int decrese = Integer.parseInt(readString("how much to decrease?: "));
            int price = Integer.parseInt(readString("from what price to decrease?: "));
            this.movieService.priceDecrease(decrese, price);
        } catch (InvalidMovieException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void handleIncreasePrice() {
        int increase = Integer.parseInt(readString("how much to increase: "));
        int price = Integer.parseInt(readString("from what price to increase?: "));
        this.movieService.priceIncrease(increase, price);
    }

    private void handleHourInterval() {
        int start = Integer.parseInt(readString("from hour: "));
        int stop = Integer.parseInt(readString("to hour: "));
        List<MovieReservation> results = this.movieReservationService.getReservationOfHourInterval(start, stop);
        for (MovieReservation movieReservation : results) {
            System.out.println(movieReservation.toString());
        }
    }

    private void handleSearch() {
        String textToSearch = readString("search: ");
        List<String> results = this.movieReservationService.search(textToSearch);
        for (String result : results) {
            System.out.println(result);
        }
    }

    private void handleSortClientCardNumbersByNumberOfReservations() {
        for (ReservationPerCardClientViewModel reservationPerCardClientViewModel :
                this.movieReservationService.getCardNumbersSortedByNrReservations()) {
            System.out.println(reservationPerCardClientViewModel.toString());
        }
    }

    private void handleSortMoviesByNumberOfReservations() {
        for (ReservationsPerMovieViewModel reservationsPerMovieViewModel :
                this.movieReservationService.getAllSortedByNumberOfReservations()) {
            System.out.println(reservationsPerMovieViewModel.toString());
        }
    }

    private void handleShowAllReservations() {
        for (MovieReservation movieReservation : this.movieReservationService.getAllReservations()) {
            System.out.println(movieReservation.toString());
        }
    }

    private void handleShowAllMovies() {
        for (Movie movie : this.movieService.getAll()) {
            System.out.println(movie.toString());
        }
    }

    private void handleAddReservation() {
        try {

            int movieId = Integer.parseInt(readString("Give movie ID:"));
            int clientCardNumber = Integer.parseInt(readString("Give client Card Number:"));
            int hourOfStart = Integer.parseInt(readString("Give hour of start:"));
            int reservationYear = Integer.parseInt(readString("Give year:"));
            int reservationMonth = Integer.parseInt(readString("Give month: "));
            int reservationDay = Integer.parseInt(readString("Give day:"));
            LocalDate dateOfreservation = LocalDate.of(reservationYear, reservationMonth, reservationDay);
            this.movieReservationService.addReservation(movieId, clientCardNumber, hourOfStart, dateOfreservation);
        } catch (WrongIdException | InvalidReservationException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void handleAddMovie() {
        try {

            String movieTitle = readString("Give movie title: ");
            int yearOfRelease = Integer.parseInt(readString("Give year of release: "));
            int price = Integer.parseInt(readString("Give ticket price: "));
            this.movieService.addMovie(movieTitle, yearOfRelease, price);
        } catch (WrongIdException | InvalidMovieException e) {
            e.printStackTrace();//log exceptions!!!!
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private char showMenu() {
        System.out.println("1. Add movie");
        System.out.println("2. Add movie reservation");
        System.out.println("3. Show all movies");
        System.out.println("4. Show all reservations");
        System.out.println("5. Show all movies sorted by number of reservations");
        System.out.println("6. Show all Client card numbers sorted by number of reservations");
        System.out.println("7. Search in everything");
        System.out.println("8. Show reservations of an hour interval");
        System.out.println("9. Increase price");
        System.out.println("a. Decrease price");
        System.out.println("b. Remove reservations between an interval");
        System.out.println("q. Undo");
        System.out.println("w. Redo");
        System.out.println("x. Exit");
        return readString("Choose option: ").charAt(0);
    }
}
