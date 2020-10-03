package Cinema;

import Cinema.Domain.Validator.BookingValidator;
import Cinema.Domain.Validator.MovieValidator;
import Cinema.Repository.BookingRepository;
import Cinema.Repository.History.History;
import Cinema.Repository.MovieRepository;
import Cinema.Repository.RepositoryManager;
import Cinema.Repository.Storage.MemoryStorage;
import Cinema.Service.BookingService;
import Cinema.Service.MovieService;
import Cinema.UserInterface.Console;
import Cinema.Util.InputOutput;

public class Main {
    /**
     *
     * Driver code.
     * 
     * @param args
     */
    public static void main(String[] args) {
        MovieRepository movieRepository = new MovieRepository(new History(), new History(), new MemoryStorage());
        BookingRepository bookingRepository = new BookingRepository(new History(), new History(), new MemoryStorage());

        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.addRepository(movieRepository);
        repositoryManager.addRepository(bookingRepository);

        MovieValidator movieValidator = new MovieValidator();
        MovieService movieService = new MovieService(repositoryManager, movieValidator);

        BookingValidator bookingValidator = new BookingValidator();
        BookingService bookingService = new BookingService(repositoryManager, bookingValidator);

        Console console = new Console(movieService, bookingService, new InputOutput());
        console.runConsole(true);
    }
}

