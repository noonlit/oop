package Cinema.UserInterface;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Booking;
import Cinema.Domain.Movie;
import Cinema.Repository.History.HistoryAwareInterface;
import Cinema.Service.BookingService;
import Cinema.Service.MovieService;
import Cinema.Util.DateConverter;
import Cinema.Util.InputOutput;
import Cinema.ViewModel.CustomerCardNumberAndBookingCount;
import Cinema.ViewModel.MovieAndBookingCount;

import java.util.ArrayList;
import java.util.List;

public class Console {
    private BookingService bookingService;
    private MovieService movieService;
    private InputOutput inputOutput;
    private HistoryAwareInterface lastUsedService;
    private List<MenuOption> menuOptions = new ArrayList<>();
    private boolean devMode = false;

    public Console(MovieService movieService, BookingService bookingService, InputOutput inputOutput) {
        this.movieService = movieService;
        this.bookingService = bookingService;
        this.inputOutput = inputOutput;
        buildMenu();
    }

    private void buildMenu() {
        addMenuOption("Exit", null);
        addMenuOption("Add new movie", this::handleNewMovie);
        addMenuOption("Find a movie", this::handleFindMovie);
        addMenuOption("Find all movies", this::handleFindMovies);
        addMenuOption("List movies sorted by bookings count", this::handleShowMoviesByBookingsCount);
        addMenuOption("Update movie", this::handleUpdateMovie);
        addMenuOption("Update price for movies with a price lower than a threshold", this::handleUpdateMoviesWithNewPrice);
        addMenuOption("Delete movie", this::handleDeleteMovie);
        addMenuOption("Add new booking", this::handleNewBooking);
        addMenuOption("Find booking", this::handleFindBooking);
        addMenuOption("Find all bookings", this::handleFindBookings);
        addMenuOption("Find bookings in interval", this::handleFindBookingsInInterval);
        addMenuOption("Update booking", this::handleUpdateBooking);
        addMenuOption("Delete booking", this::handleDeleteBooking);
        addMenuOption("Delete bookings in interval", this::handleDeleteBookingsInInterval);
        addMenuOption("Show customer card numbers ordered by number of bookings", this::handleShowCustomerCardNumbersByBookingsCount);
        addMenuOption("Undo previous insert/delete/update", this::handleUndo);
        addMenuOption("Redo previous insert/delete/update", this::handleRedo);
    }

    /**
     * Adds a new option to the application menu.
     * @param optionText The text to display for the user
     * @param command The method to run when the option is chosen.
     */
    private void addMenuOption(String optionText, Runnable command) {
        menuOptions.add(new MenuOption(optionText, command));
    }

    /**
     * Displays the application menu.
     */
    private void showMenu() {
        int index = 0;
        for (MenuOption option : menuOptions) {
            inputOutput.println(index + ". " + option.getOptionText());
            index++;
        }

        inputOutput.println("Your option: ");
    }

    /**
     * Displays the menu, reads the client option, executes the option.
     */
    public void runConsole(boolean devMode) {
        this.devMode = devMode;

        MenuOption option;
        while (true) {
            inputOutput.println("-".repeat(40));
            showMenu();

            int userOption = inputOutput.readInt();
            option = menuOptions.get(userOption);

            if (option == null) {
                inputOutput.printErrln("Not implemented or invalid option!");
                continue;
            }

            Runnable command = option.getCommand();
            if (command == null) {
                inputOutput.println("Exiting application.");
                return;
            }

            command.run();
            inputOutput.println("-".repeat(40));
        }
    }

    /**
     * Displays customer card numbers sorted by the bookings count.
     */
    private void handleShowCustomerCardNumbersByBookingsCount() {
        List<CustomerCardNumberAndBookingCount> bookingData = bookingService.getCustomerCardNumbersSortedByBookingsCount();

        if (bookingData.isEmpty()) {
            inputOutput.println("No results.");
            return;
        }

        for (CustomerCardNumberAndBookingCount data : bookingData) {
            inputOutput.println(data.toString());
        }
    }

    /**
     * Displays movies sorted by the bookings count.
     */
    private void handleShowMoviesByBookingsCount() {
        List<MovieAndBookingCount> movieData = bookingService.getMoviesSortedByBookingsCount();

        if (movieData.isEmpty()) {
            inputOutput.println("No results.");
            return;
        }

        for (MovieAndBookingCount data : movieData) {
            inputOutput.println(data.toString());
        }
    }

    /**
     * Displays bookings in a given hourly interval, regardless of the date.
     */
    private void handleFindBookingsInInterval() {
        inputOutput.println(String.format("Start time, e.g. %s", DateConverter.timeFormatExample));
        String start = inputOutput.readLine();

        inputOutput.println(String.format("End time, e.g. %s", DateConverter.timeFormatExample));
        String end = inputOutput.readLine();

        List<Booking> bookings = bookingService.find(start, end);

        if (bookings.isEmpty()) {
            inputOutput.println("No results.");
            return;
        }

        for (AbstractEntity booking : bookings) {
            inputOutput.println(booking.toString());
        }
    }

    /**
     * Finds and displays bookings by a given query. The query can be the customer card ID or the movie title.
     */
    private void handleFindBookings() {
        inputOutput.println("Your query:" );
        String query = inputOutput.readLine();

        List<AbstractEntity> bookings = bookingService.find(query);

        if (bookings.isEmpty()) {
            inputOutput.println("No results.");
            return;
        }

        for (AbstractEntity booking : bookings) {
            inputOutput.println(booking.toString());
        }
    }

    /**
     * Deletes a booking with the given ID.
     */
    private void handleDeleteBooking() {
        inputOutput.println("Booking ID: ");
        int id = inputOutput.readInt();

        try {
            boolean deleted = bookingService.delete(id);
            lastUsedService = bookingService;

            String message = deleted ? "The booking was deleted!" : "The booking was not deleted!";
            inputOutput.println(message);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates a booking by ID.
     */
    private void handleUpdateBooking() {
        inputOutput.println("Booking ID: ");
        int id = inputOutput.readInt();

        inputOutput.println("Movie title: " );
        String title = inputOutput.readLine();

        inputOutput.println("Customer card: ");
        int card = inputOutput.readInt();

        inputOutput.println(String.format("Date and time, e.g. %s", DateConverter.dateTimeFormatExample));
        String date = inputOutput.readLine();

        try {
            Booking booking = bookingService.updateBooking(id, title, card, date);
            lastUsedService = movieService;

            inputOutput.println("Booking was updated! " + booking);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Finds and displays a single booking by a given query. The query can be the customer card ID or the movie title.
     */
    private void handleFindBooking() {
        inputOutput.println("Your query: " );
        String query = inputOutput.readLine();

        AbstractEntity booking = bookingService.findOne(query);
        if (booking == null) {
            inputOutput.println("No results.");
            return;
        }

        inputOutput.println(booking.toString());
    }

    /**
     * Saves a new booking.
     */
    private void handleNewBooking() {
        inputOutput.println("Movie title: " );
        String title = inputOutput.readLine();

        inputOutput.println("Customer card: ");
        int card = inputOutput.readInt();

        inputOutput.println(String.format("Date and time, e.g. %s", DateConverter.dateTimeFormatExample));
        String date = inputOutput.readLine();

        try {
            Booking booking = bookingService.createBooking(title, card, date);
            lastUsedService = movieService;

            inputOutput.println("New booking was added! " + booking);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Deletes bookings between given datetimes and displays the count of deleted entities.
     */
    private void handleDeleteBookingsInInterval() {
        inputOutput.println(String.format("Start date and time, e.g. %s", DateConverter.dateTimeFormatExample));
        String startDate = inputOutput.readLine();

        inputOutput.println(String.format("End date and time, e.g. %s", DateConverter.dateTimeFormatExample));
        String endDate = inputOutput.readLine();

        int deletedEntriesCount = bookingService.delete(startDate, endDate);
        lastUsedService = bookingService;

        inputOutput.println(String.format("Deleted %d bookings.", deletedEntriesCount));
    }

    /**
     * Finds and displays list of movies by the given query and prints the result.
     */
    private void handleFindMovies() {
        inputOutput.println("Your query:" );
        String query = inputOutput.readLine();

        List<AbstractEntity> movies = movieService.find(query);

        if (movies.isEmpty()) {
            inputOutput.println("No results.");
            return;
        }

        for (AbstractEntity movie : movies) {
            inputOutput.println(movie.toString());
        }
    }

    /**
     * Undoes the last create/update/delete.
     */
    private void handleUndo() {
        if (lastUsedService == null) {
            inputOutput.println("There is no operation to undo!");
            return;
        }

        try {
            boolean reverted = lastUsedService.undo();
            String message = reverted ? "The operation was undone!" : "The operation was not undone!";
            inputOutput.println(message);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Redoes the last create/update/delete.
     */
    private void handleRedo() {
        if (lastUsedService == null) {
            inputOutput.println("There is no operation to redo!");
            return;
        }

        try {
            boolean reverted = lastUsedService.redo();
            String message = reverted ? "The operation was redone!" : "The operation was not redone!";
            inputOutput.println(message);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Deletes a movie and prints a confirmation message.
     */
    private void handleDeleteMovie() {
        inputOutput.println("Movie ID: ");
        int id = inputOutput.readInt();

        try {
            boolean deleted = movieService.delete(id);
            lastUsedService = movieService;

            String message = deleted ? "The movie was deleted!" : "The movie was not deleted!";
            inputOutput.println(message);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates an existing movie and outputs the result.
     */
    private void handleUpdateMovie() {
        inputOutput.println("Movie ID: ");
        int id = inputOutput.readInt();

        inputOutput.println("Movie title:" );
        String title = inputOutput.readLine();

        inputOutput.println("Movie year: ");
        int year = inputOutput.readInt();

        inputOutput.println("Ticket price: ");
        int price = inputOutput.readInt();

        inputOutput.println("Is the movie running? (type Y for yes, any other character for no)");
        String isRunningOption = inputOutput.readLine();
        boolean isRunning = isRunningOption.equalsIgnoreCase("Y");

        try {
            Movie movie = movieService.updateMovie(id, title, year, price, isRunning);
            lastUsedService = movieService;

            inputOutput.println("Movie was updated! " + movie);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates movies with a price beneath a threshold with a new price.
     */
    private void handleUpdateMoviesWithNewPrice() {
        inputOutput.println("The price threshold (all movies with a price lower than this value will be updated): ");
        int price = inputOutput.readInt();
        inputOutput.println("The new price for the movies: ");
        int newPrice = inputOutput.readInt();

        try {
            int updatedMoviesCount = movieService.updateMoviesPrice(price, newPrice);
            lastUsedService = movieService;

            inputOutput.println(String.format("Updated %d movies", updatedMoviesCount));
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Finds and displays movie by a given query string.
     */
    private void handleFindMovie() {
        inputOutput.println("Your query:" );
        String query = inputOutput.readLine();

        AbstractEntity movie = movieService.findOne(query);

        if (movie == null) {
            inputOutput.println("No results.");
            return;
        }

        inputOutput.println(movie.toString());
    }

    /**
     * Creates a new movie and outputs the result.
     */
    private void handleNewMovie() {
        inputOutput.println("Movie title:" );
        String title = inputOutput.readLine();

        inputOutput.println("Movie year: ");
        int year = inputOutput.readInt();

        inputOutput.println("Ticket price: ");
        int price = inputOutput.readInt();

        inputOutput.println("Is the movie running? (type Y for yes, any other character for no)");
        String isRunningOption = inputOutput.readLine();
        boolean isRunning = isRunningOption.equalsIgnoreCase("Y");

        try {
            Movie movie = movieService.createMovie(title, year, price, isRunning);
            lastUsedService = movieService;

            inputOutput.println("New movie was added! " + movie);
        } catch (Exception ex) {
            inputOutput.printErrln("We have errors:");
            inputOutput.printErrln(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }
}
